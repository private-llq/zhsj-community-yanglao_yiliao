package com.zhsj.community.yanglao_yiliao.old_activity.controller;



import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.common.pageVoed;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import com.zhsj.community.yanglao_yiliao.old_activity.util.MyPageUtils;
import com.zhsj.community.yanglao_yiliao.old_activity.util.PageInfo;
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
     * @param reqBo 用户id，经度，维度
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 获取附近活动列表
     * @author liulq
     * @date 2021/11/24 19:00
     */
    @PostMapping("/queryActivityList")
    public R<List<ActivityDto>> queryActivityList(@RequestBody @Valid ActivityReqBo reqBo) {
        log.info("reqBo的值{}", reqBo);
        List<ActivityDto> rspList = this.activityService.queryActivityList(reqBo);
        return R.ok(rspList);
    }


    /**
     * @param activityReqVoDto 用户id，经度，维度
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 获取附近活动
     * @author liulq
     * @date 2021/11/24 19:00
     */
    @PostMapping("/queryActivity")
    public PageInfo<ActivityDto> queryActivity(@RequestBody @Valid ActivityReqVoDto activityReqVoDto) {
        log.info("activityReqVo的值{}", activityReqVoDto);
        List<ActivityDto> activityDtos = this.activityService.queryActivity(activityReqVoDto);
        PageInfo<ActivityDto> activityDtoPageInfo = MyPageUtils.pageMap(activityReqVoDto.getPage(), activityReqVoDto.getData(), activityDtos);
        return activityDtoPageInfo;
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
        log.info("id的值{}", id);
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
        log.info("reqBo的值{}", reqBo);
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
        List<ActivityType> list = this.activityTypeService.selectList();
        return R.ok(list);
    }

    /**
     * @description 查询活动的类型
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("getActivity")
    public R getActivityType() {
        List<ActivityFromDto> getactivit = this.activityService.getactivit();
        return R.ok(getactivit);
    }

    /**
     * @description 查询自己的所有活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("getUserActivityList")
    public PageInfo<ActivityDto> getUserActivityList(@RequestBody @Valid ActivityPageDto activityPageDto) {
        log.info("页码：{}", activityPageDto);
        List<ActivityDto> userActivityList = this.activityService.getUserActivityList(activityPageDto);
        PageInfo<ActivityDto> activityDtoPageInfo = MyPageUtils.pageMap(activityPageDto.getPage(), activityPageDto.getData(), userActivityList);
        return activityDtoPageInfo;
    }


    /**
     * @description 分页查询所有活动
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("pageListed")
    public PageInfo<ActivityDto> pageListed(@RequestBody @Valid ActivityReqBo activityReqBo) {
        log.info("activityReqBo的值{}", activityReqBo);
        List<ActivityDto> activityDtos = this.activityService.pageListed(activityReqBo);
        PageInfo<ActivityDto> activityDtoPageInfo = MyPageUtils.pageMap(activityReqBo.getPage(), activityReqBo.getData(), activityDtos);
        return activityDtoPageInfo;
    }

    /**
     * @description 根据别人的id查询活动详情
     * @author liulq
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("getActivityedPageList")
    public R<?> getActivityedge(@RequestBody @Valid ActivityPageDto activityPageDto) {
        log.info("activityPageDto的值{}", activityPageDto);
        List<ActivityDto> activityDtos = this.activityService.getActivityePagelist(activityPageDto);
        PageInfo<ActivityDto> getActivityedge = MyPageUtils.pageMap(activityPageDto.getPage(), activityPageDto.getData(), activityDtos);
        return R.ok(getActivityedge);
    }


    /*****************************************************大后台*******************************************************/

    /**
     * @Description: 新增活动类型
     * @Param:
     * @return:
     * @Author: liulq
     * @date: 2021-12-06
     */
    @PostMapping("addActivityType")
    public R addActivityType(@RequestBody @Valid ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDto的值：{}", activityTypeDto);
        this.activityTypeService.ActivityType(activityTypeDto);
        return R.ok("新增活动类型成功");
    }


    /**
     * @Description: 修改活动类型
     * @Param:
     * @return:
     * @Author: liulq
     * @date: 2021-12-06
     */
    @PostMapping("updateActivityType")
    public R updateActivityType(@RequestBody @Valid ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDto的值：{}", activityTypeDto);
        this.activityTypeService.updateActivityType(activityTypeDto);
        return R.ok("修改活动类型成功");
    }

    /**
    *@Description: 删除活动类型
    *@Param:  * @param null
    *@return: 
    *@Author: liulq
    *@date: 2021-12-06
    */
    @DeleteMapping("deleteActivityType")
    public R deleteActivityType(@RequestParam  String activityTypeCode){
        log.info("activityTypeCode的值：{}", activityTypeCode);
         this.activityTypeService.deleteActivityType(activityTypeCode);
        return R.ok("删除成功");
    }

    /**
     * 大后台展示活动信息
     * @param pageVoed
     * @return
     */
    @PostMapping("selectActivityList")
    public R<?> selectActivityList(@RequestBody  pageVoed pageVoed){
        List<ActivityReqDto> activityReqDtos = this.activityTypeService.selectActivityList();
        PageInfo<ActivityReqDto> activityReqDtoPageInfo = MyPageUtils.pageMap(pageVoed.getPage(), pageVoed.getData(), activityReqDtos);
        return R.ok(activityReqDtoPageInfo);
    }


    /***
     * 大后台根据id删除活动
     * @param id
     * @return
     */
    @DeleteMapping("/deleteActivityById")
    public R<String> deleteActivityById(@RequestParam Long id) {
        log.info("id的值{}", id);
        this.activityService.deleteById(id);
        return R.ok("删除成功！");
    }

    /**
     * 大后台模糊查询活动
     * @param likeActivity
     * @return
     */
    @PostMapping("likeActivity")
    public R likeActivity(@RequestBody LikeActivityDto likeActivity){
        log.info("likeActivity的值{}", likeActivity);
        this.activityService.likeActivity(likeActivity);
        return R.ok();
    }





}
