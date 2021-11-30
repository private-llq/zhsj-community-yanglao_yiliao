package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityDetailsMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityDetails;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityService;
import com.zhsj.community.yanglao_yiliao.old_activity.util.GouldUtil;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityReqVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
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

    @Autowired
    private ActivityDetailsMapper activityDetailsMapper;

    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER)
    @Autowired
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;


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
        List<ActivityDto> activityDtos = this.activityMapper.queryNearbyActivityList(reqBo);
        for (ActivityDto activity: activityDtos){
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime,now).toMinutes();
            activity.setPublishTimed(minutes);
        }
        return activityDtos;
    }

    @Override
    public List<ActivityDto> queryActivity(ActivityReqVo activityReqVo) {
        List<ActivityDto> activityDtos = this.activityMapper.queryActivityList(activityReqVo);
        for (ActivityDto activity :activityDtos){
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    activityReqVo.getLatitude() + "," + activityReqVo.getLongitude());
            activity.setDistance(apiDistance / 1000);
            LocalDateTime now = LocalDateTime.now();
            for (ActivityDto activityed: activityDtos){
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime,now).toMinutes();
                activityed.setPublishTimed(minutes);
            }
        }
        return activityDtos;
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
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(loginUser.getId());
        Activity activity = new Activity();
        //辅助相同属性
        BeanUtils.copyProperties(reqBo,activity);
        LocalDateTime now = LocalDateTime.now();
        //默认不是好友  后期能查询 再调整 2021-11-23
        activity.setUserUuid(loginUser.getId().toString());
        activity.setUserName(loginUser.getNickName());
       activity.setAvatarImages(userDetail.getAvatarThumbnail());
        activity.setIsFriend(false);
        activity.setDeleted(true);
        activity.setIsUser(true);
        activity.setPublishTime(now);
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        //新增详情表
        ActivityDetails activityDetails = new ActivityDetails();
        activityDetails.setAId(loginUser.getId());
        activityDetails.setNickname(loginUser.getNickName());
        this.activityDetailsMapper.insert(activityDetails);
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
     * @param
     * @return
     */
    @Override
    public IPage<Activity> getActivityList(Page<Activity> page) {
        log.info("用户的uid{}",page);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Page<Activity> activities = this.activityMapper.selectPage(page, new QueryWrapper<Activity>().eq("user_uuid", loginUser.getId()));
               return activities;
    }

    @Override
    public List<ActivityDto> pageListed(ActivityReqBo activityDto) {
        List<ActivityDto> activityDtos = this.activityMapper.pageListed(activityDto);
        for (ActivityDto activity :activityDtos){
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    activityDto.getLatitude() + "," + activityDto.getLongitude());
            activity.setDistance(apiDistance / 1000);
        }
        for (ActivityDto activity: activityDtos){
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime,now).toMinutes();
            activity.setPublishTimed(minutes);
        }
        return activityDtos;
    }

    @Override
    public List<ActivityDto> getActivityePagelist(ActivityPageDto activityPageDto) {
        List<ActivityDto> activityDtos = this.activityMapper.getActivityePagelist(activityPageDto);
        for (ActivityDto activity :activityDtos){
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    activityPageDto.getLatitude() + "," + activityPageDto.getLongitude());
            activity.setDistance(apiDistance / 1000);
            LocalDateTime now = LocalDateTime.now();
            for (ActivityDto activityed: activityDtos){
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime,now).toMinutes();
                activityed.setPublishTimed(minutes);
            }
        }
        return activityDtos;
    }


}
