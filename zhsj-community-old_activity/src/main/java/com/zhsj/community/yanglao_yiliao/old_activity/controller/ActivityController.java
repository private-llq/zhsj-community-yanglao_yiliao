package com.zhsj.community.yanglao_yiliao.old_activity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.common.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import java.util.List;


/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description: 活动相关的接口
 * @create: 2021-11-10 17:03
 */
@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityTypeService activityTypeService;


    /**
     * @description 获取附近活动列表
     * @author liulq
     * @date 2021/11/24 19:00
     * @param reqBo 用户id，经度，维度
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     */
    @PostMapping("/queryActivityList")
    public R<List<ActivityDto>> queryActivityList(@RequestBody @Valid ActivityReqBo reqBo) {
        List<ActivityDto> rspList = this.activityService.queryActivityList(reqBo);
        return R.ok(rspList);
    }


    /**
     * @description 删除活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @DeleteMapping("/deleteActivity")
    public R<String> deleteActivity(@RequestParam Long id) {
        this.activityService.delete(id);
        return R.ok("删除成功！");
    }

    /**
     * @description 发布活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("/publishActivity")
    public R<String> publishActivity(@RequestBody @Valid ActivitySaveReqBo reqBo) {
        this.activityService.publishActivity(reqBo);
        return R.ok("发布成功！");
    }

    /**
     * @description 查询活动类型
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("/queryActivityTypeList")
    public R<List<ActivityType>> queryActivityTypeList() {
        List<ActivityType> list =  this.activityTypeService.selectList();
        return R.ok(list);
    }


    /**
     * 查询活动的类型
     */
    @GetMapping("getActivity")
    public Result getActivityType() {
        List<ActivityFromDto> getactivit = this.activityService.getactivit();
        return Result.ok(getactivit);
    }

    /**
     * 查询自己的所有活动
     *
     */
    @GetMapping("getUserActivityList")
    public Result getUserActivityList(@RequestParam Long uid){
        log.info("用户的uid{}",uid);
        List<Activity> activityList = this.activityService.getActivityList(uid);
        return Result.ok(activityList);
    }

    /**
     * @Description: 分页查询
     * @author: liulq
     *
     */
    @PostMapping("pageList")
    public PageList<Activity> pageList(@RequestBody pageVo pageVo) {
        log.info("分页参数{}",pageVo);
        Page<Activity> page = new Page<>(pageVo.getPage(), pageVo.getRows());
        page = this.activityService.page(page);
        return new PageList<>(page.getTotal(), page.getRecords());
    }


    /**
     * 根据id查询所有活动
     * @param id
     * @return
     */
    @GetMapping("getActivityListOther")
    public Result getActivityListOther(@RequestParam Long id){
        log.info("id的值{}",id);
        Activity activityListOther = this.activityService.getActivityListOther(id);
        return Result.ok(activityListOther);
    }

    /**
     * 根据别人的id查询活动详情
     * @param id
     * @return
     */
    @GetMapping("getActivityedge")
    public Result  getActivityedge(@RequestParam Long id){
        log.info("id的值{}",id);
        List<ActivityListDto> activityedge = this.activityService.getActivityedge(id);
       return Result.ok(activityedge);
    }



}
