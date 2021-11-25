package com.zhsj.community.yanglao_yiliao.old_activity.controller;
import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.old_activity.jo.ActivityReqBo;
import com.zhsj.community.yanglao_yiliao.old_activity.jo.ActivitySaveReqBo;
import com.zhsj.community.yanglao_yiliao.old_activity.common.*;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.*;
import com.zhsj.community.yanglao_yiliao.old_activity.po.ActivityType;
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
     * @author chengl
     * @date 2021/11/24 19:00
     * @param reqBo 用户id，经度，维度
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     */
    @PostMapping("/queryActivityList")
    public R<List<ActivityDto>> queryActivityList(@RequestBody @Valid ActivityReqBo reqBo) {
        List<ActivityDto> rspList = activityService.queryActivityList(reqBo);
        return R.ok(rspList);
    }


    /**
     * @description 删除活动
     * @author chengl
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @DeleteMapping("/deleteActivity")
    public R<String> deleteActivity(@RequestParam Long id) {
        activityService.delete(id);
        return R.ok("删除成功！");
    }

    /**
     * @description 发布活动
     * @author chengl
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @PostMapping("/publishActivity")
    public R<String> publishActivity(@RequestBody @Valid ActivitySaveReqBo reqBo) {
        activityService.publishActivity(reqBo);
        return R.ok("发布成功！");
    }

    /**
     * @description 查询活动类型
     * @author chengl
     * @date 2021/11/24 19:00
     * @Param: [id]
     * @return: com.zhsj.basecommon.vo.R<java.lang.Void>
     */
    @GetMapping("/queryActivityTypeList")
    public R<List<ActivityType>> queryActivityTypeList() {
        List<ActivityType> list =  activityTypeService.selectList();
        return R.ok(list);
    }



    /**===============================================================================================================*/

    /**
     * 查询活动的类型
     */
    @GetMapping("get")
    public Result getActivityType() {
        List<ActivityFrom> getactivited = this.activityService.getactivit();
        return Result.ok(getactivited);
    }

//    /**
//     * 删除活动
//     */
//    @DeleteMapping("delete")
//    public Result deleteActivity(@RequestParam Long uid) {
//        log.info("id{}", uid);
//        this.activityService.deletedActivity(uid);
//        return Result.ok();
//    }

//    /**
//     * 新增活动
//     */
//    @PostMapping("add")
//    public Result addActivity(@RequestBody @Validated addActivityFrom addActivityFrom) {
//        log.info("获取参数{}", addActivityFrom);
//        int i = this.activityService.addActivity(addActivityFrom);
//        return Result.ok(i);
//    }
//
//    /**
//     * 点击头像查询查看活动、个人资料
//     *
//     */
//    @GetMapping("pageList")
//    public Result pageActivity(@RequestBody PageResult pageResult) {
//        log.info("页数{}",pageResult);
//        Page<Activityed> activityPage = this.activityService.queryAlbumList(pageResult);
//        return Result.ok(activityPage);
//    }
//
//    /**
//     *编辑资料
//     *
//     */
//    @PutMapping("update")
//    public Result  updateUserInfoByUserId(@RequestBody ActivityUpdateFrom addActivityUpdateFrom){
//        log.info("参数{}",addActivityUpdateFrom);
//        int i = this.activityService.updateUserInfo(addActivityUpdateFrom);
//        return Result.ok(i);
//    }
//
//
//    /**
//     * 查询附近的活动或者好友的活动
//     *
//     *
//     */
//    @GetMapping("select")
//    public Result selectActivity(@RequestBody  @Validated UserLocationFrom userLocationFrom){
//        log.info("附近的人参数{}",userLocationFrom);
//        HashSet<LinkedList<UserLocation>> linkedLists = this.activityService.listActivities(userLocationFrom);
//        return  Result.ok(linkedLists);
//    }
//

}
