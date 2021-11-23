package com.zhsj.community.yanglao_yiliao.old_activity.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.community.yanglao_yiliao.old_activity.common.*;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description: 活动相关的接口
 * @create: 2021-11-10 17:03
 */
@RestController
@RequestMapping("activity")
@Slf4j
public class ActivityController {


    @Resource
    private ActivityService activityService;

    /**
     * 查询活动的类型
     */
    @GetMapping("get")
    public Result getActivityType() {
        List<ActivityFrom> getactivited = this.activityService.getactivit();
        return Result.ok(getactivited);
    }

    /**
     * 删除活动
     */
    @DeleteMapping("delete")
    public Result deleteActivity(@RequestParam Long uid) {
        log.info("id{}", uid);
        this.activityService.deletedActivity(uid);
        return Result.ok();
    }

    /**
     * 新增活动
     */
    @PostMapping("add")
    public Result addActivity(@RequestBody @Validated addActivityFrom addActivityFrom) {
        log.info("获取参数{}", addActivityFrom);
        int i = this.activityService.addActivity(addActivityFrom);
        return Result.ok(i);
    }

    /**
     * 点击头像查询查看活动、个人资料
     *
     */
    @GetMapping("pageList")
    public Result pageActivity(@RequestBody PageResult pageResult) {
        log.info("页数{}",pageResult);
        Page<Activity> activityPage = this.activityService.queryAlbumList(pageResult);
        return Result.ok(activityPage);
    }

    /**
     *编辑资料
     *
     */
    @PutMapping("update")
    public Result  updateUserInfoByUserId(@RequestBody ActivityUpdateFrom addActivityUpdateFrom){
        log.info("参数{}",addActivityUpdateFrom);
        int i = this.activityService.updateUserInfo(addActivityUpdateFrom);
        return Result.ok(i);
    }


    /**
     * 查询附近的活动或者好友的活动
     *
     *
     */
    @GetMapping("select")
    public Result selectActivity(@RequestBody  @Validated UserLocationFrom userLocationFrom){
        log.info("附近的人参数{}",userLocationFrom);
        HashSet<LinkedList<UserLocation>> linkedLists = this.activityService.listActivities(userLocationFrom);
        return  Result.ok(linkedLists);
    }


}
