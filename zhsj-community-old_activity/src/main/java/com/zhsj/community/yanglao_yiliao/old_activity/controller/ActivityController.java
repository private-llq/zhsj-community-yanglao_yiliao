package com.zhsj.community.yanglao_yiliao.old_activity.controller;
import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.common.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
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


    @Resource
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



}
