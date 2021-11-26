package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 活动的实现类
 * @create: 2021-11-10 17:03
 */
@Service
@Slf4j
public class ActivityServiceImpl   extends ServiceImpl <ActivityMapper,Activity> implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;


    /**
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     */
    @Override
    public List<ActivityDto> queryActivityList(ActivityReqBo reqBo){
        log.info("Activity request parameters, ActivityReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();

        //设置附近最大距离
        if(reqBo.getDist()==null){
            reqBo.setDist(10.0);
        }
        return this.activityMapper.queryNearbyActivityList(reqBo);
    }

    /**
     * @Description: 删除
     * @author: liulq
     * @date 2021/11/23 10:43
     * @Param: [id]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("id 的值{}",id);
        this.activityMapper.deleteById(id);
    }
    /**
     * @Description: 新增
     * @author: liulq
     * @date 2021/11/23 10:43
     * @Param: [id]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publishActivity(ActivitySaveReqBo reqBo) {
        log.info("Activity request parameters, ActivitySaveReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Activity activity = new Activity();

        //辅助相同属性
        BeanUtils.copyProperties(reqBo,activity);
        LocalDateTime now = LocalDateTime.now();
        //默认不是好友  后期能查询 再调整 2021-11-23
        activity.setUserUuid(loginUser.getId().toString());
        activity.setUserName(loginUser.getNickName());
        activity.setIsFriend(false);
        activity.setDeleted(true);
        activity.setPublishTime(now);
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        return this.activityMapper.insert(activity);
    }



    /**
     * 查询活动类型
     *
     */
    @Override
    public List<ActivityFromDto> getactivit() {
        return this.activityMapper.getActivityTyped();
    }

    /**
     * 查询自己的所有活动
     * @param uid
     * @return
     */
    @Override
    public List<Activity> getActivityList(Long uid) {
        log.info("用户的uid{}",uid);
        List<Activity> activities = this.activityMapper.selectList(new QueryWrapper<Activity>().eq("user_uuid", uid));
               return activities;
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    @Override
    public Activity getActivityListOther(Long id) {
        log.info("id的值{}",id);
        Activity activity = this.activityMapper.selectById(id);
        return activity;
    }

    /**
     * 查询别人的详情
     * @param id
     * @return
     */
    @Override
    public List<ActivityListDto> getActivityedge(Long id) {
        log.info("id的值{}",id);
        List<ActivityListDto> activityedge = this.activityMapper.getActivityedge(id);
        return activityedge;
    }


}
