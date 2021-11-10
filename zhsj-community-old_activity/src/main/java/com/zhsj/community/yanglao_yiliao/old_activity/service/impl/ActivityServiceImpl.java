package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:03
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 查询活动的类型
     * @param loginUser
     * @return
     */
    @Override
    public List<Activity> getType(LoginUser  loginUser) {
        log.info("用户的信息{}",loginUser);
        List<Activity> activity = activityMapper.selectList(new QueryWrapper<Activity>()
                .eq("u_id", loginUser.getCurrentIp()));
        System.out.println("这个的值是："+activity);
        return activity;
    }
}
