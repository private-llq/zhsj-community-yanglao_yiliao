package com.zhsj.community.yanglao_yiliao.old_activity.controller;


import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description:
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
     *
     */
    @GetMapping("get")
    public List<ActivityFrom> getActivityType() {
        List<ActivityFrom> getactivit = this.activityService.getactivit();
        return  getactivit;
    }

    /**
     * 删除活动
     *
     */
    @DeleteMapping("delete")
    public int deleteActivity(@RequestBody @Validated LoginUser loginUser){
        log.info("用户的信息{}",loginUser);
        return this.activityService.deletedActivity(loginUser);
    }

    /**
     * 新增活动
     */
    @PostMapping("add")
    public int addActivity(@RequestParam("voice") String voice,@RequestParam("textContent") String textContent,
                           @RequestParam("location") String location,@RequestParam("longitude") String longitude,
                           @RequestParam("latitude") String latitude,@RequestParam("multipartFile") String multipartFile){
        log.info("获取参数{},{},{},{},{},{}",voice,textContent,location,longitude,latitude,multipartFile);
        return  activityService.addActivity(voice, textContent, longitude, location, latitude, multipartFile);
    }
}
