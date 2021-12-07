package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.base.api.vo.PageVO;
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
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityDetailsMapper activityDetailsMapper;


    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER)
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;


    /**
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     */
    @Override
    public List<ActivityDto> queryActivityList(ActivityReqBo reqBo) {
        log.info("Activity request parameters, ActivityReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        LocalDateTime now = LocalDateTime.now();
        List<ActivityDto> activityDtos = this.activityMapper.queryNearbyActivityList(reqBo);
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getId());
        for (ActivityDto activity : activityDtos) {
            if (activity.getUserUuid().equals(userAuth().getAccount())) {
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        reqBo.getLatitude() + "," + reqBo.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                activity.setAge(userDetail.getAge());
                activity.setImId(userAuth().getImId());
                activity.setUserSelf(1);
            }else {
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        reqBo.getLatitude() + "," + reqBo.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                activity.setAge(userDetail.getAge());
                activity.setImId(userAuth().getImId());
                activity.setUserSelf(0);
            }
        }
        return activityDtos;
    }

    /**
     * 获取附近活动
     *
     * @param activityReqVoDto 用户id，查询时间类型
     */
    @Override
    public List<ActivityDto> queryActivity(ActivityReqVoDto activityReqVoDto) {
        log.info("activityReqVo的参数{}", activityReqVoDto);
        List<ActivityDto> activityDtos = this.activityMapper.queryActivityList(activityReqVoDto);
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getId());
        for (ActivityDto activity : activityDtos) {
            if (activity.getUserUuid().equals(userAuth().getAccount())) {
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        activityReqVoDto.getLatitude() + "," + activityReqVoDto.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                activity.setAge(userDetail.getAge());
                activity.setImId(userAuth().getImId());
                activity.setUserSelf(1);
            } else {
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        activityReqVoDto.getLatitude() + "," + activityReqVoDto.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                activity.setAge(userDetail.getAge());
                activity.setImId(userAuth().getImId());
                activity.setUserSelf(0);
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
        log.info("id 的值{}", id);
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
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getId());
        Activity activity = new Activity();
        //辅助相同属性
        BeanUtils.copyProperties(reqBo, activity);
        LocalDateTime now = LocalDateTime.now();
        //默认不是好友  后期能查询 再调整 2021-11-23
        activity.setUserUuid(userAuth().getAccount());
        activity.setUserName(userAuth().getNickName());
        activity.setAvatarImages(userDetail.getAvatarThumbnail());
        activity.setBirthday(userDetail.getBirthday());
        activity.setIsFriend(false);
        activity.setDeleted(true);
        activity.setIsUser(true);
        activity.setPublishTime(now);
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        //新增详情表
        ActivityDetails activityDetails = new ActivityDetails();
        activityDetails.setAId(userAuth().getAccount());
        activityDetails.setNickname(userAuth().getNickName());
        activityDetails.setCreateTime(now);
        activityDetails.setUpdateTime(now);
        activityDetails.setDeleted(true);
        this.activityDetailsMapper.insert(activityDetails);
        return this.activityMapper.insert(activity);
    }



    /**
     * 查询自己的所有活动
     *
     * @param activityPageDto
     */
    @Override
    public List<ActivityDto> getUserActivityList(ActivityPageDto activityPageDto) {
        log.info("用户的uid{}", activityPageDto);
        List<ActivityDto> activityDtos = this.activityMapper.selectgetUserActivityList(activityPageDto);
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getId());
        for (ActivityDto activity : activityDtos) {
            if (activity.getUserUuid().equals(userAuth().getAccount())) {
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        activityPageDto.getLatitude() + "," + activityPageDto.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                activity.setAge(userDetail.getAge());
                activity.setImId(userAuth().getImId());
                activity.setUserSelf(1);
            } else {
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        activityPageDto.getLatitude() + "," + activityPageDto.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                activity.setAge(userDetail.getAge());
                activity.setImId(userAuth().getImId());
                activity.setUserSelf(0);
            }
        }
        return activityDtos;
    }

    /**
     * 分页查询所有活动
     *
     * @param activityReqBo
     */
    @Override
    public List<ActivityDto> pageListed(ActivityReqBo activityReqBo) {
        log.info("activityReqBo的参数{}", activityReqBo);
        List<ActivityDto> activityDtos = this.activityMapper.pageListed(activityReqBo);
        for (ActivityDto activity : activityDtos) {
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    activityReqBo.getLatitude() + "," + activityReqBo.getLongitude());
            activity.setDistance(apiDistance / 1000);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            activity.setImId(userAuth().getImId());
        }
        return activityDtos;
    }


    /**
     * 根据id删除活动
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        log.info("id 的值{}", id);
        this.activityMapper.deleteById(id);
    }

    /**
     * 模糊查询活动
     * @param likeActivity
     * @return
     */
    @Override
    public List<ActivityReqDto> likeActivity(LikeActivityDto likeActivity) {
        log.info("likeActivity 的值{}", likeActivity);
        PageVO<UserDetail> userDetailPageVO = this.iBaseUserInfoRpcService.queryUser(likeActivity.getUserName(), likeActivity.getPhone(), likeActivity.getPage(), likeActivity.getData());
        //等我在想想在写
        return null;
    }


    /**
     * ***************************************获取当前登录用户**********************************************************
     * *****************************************************************************************************
     */
    private LoginUser userAuth() {
        return ContextHolder.getContext().getLoginUser();
    }

}
