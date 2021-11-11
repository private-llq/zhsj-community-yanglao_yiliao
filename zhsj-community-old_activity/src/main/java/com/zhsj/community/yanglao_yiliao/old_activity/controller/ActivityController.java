package com.zhsj.community.yanglao_yiliao.old_activity.controller;


import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:03
 */
@RestController
@RequestMapping("activity")
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 查询活动的类型
     *
     * @return
     */
    @GetMapping("get")
    public ActivityVo getActivityType() {
        ActivityVo activityVo = this.activityService.geted();
        return activityVo;
    }

    /**
     * 删除活动
     * @param loginUser
     * @return
     */
    @DeleteMapping("delete")
    public int deleteActivity(LoginUser loginUser){
        int i = this.activityService.deletedActivity(loginUser);
        return i;
    }

}
