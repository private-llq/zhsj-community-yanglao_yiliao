package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.base.api.vo.UserImVo;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
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
        LocalDateTime now = LocalDateTime.now();
        List<ActivityDto> activityDtos = this.activityMapper.queryNearbyActivityList(reqBo);
        for (ActivityDto activity : activityDtos) {
            long apiDistance = GouldUtil.getApiDistance(activity.getLongitude() + "," + activity.getLatitude(),
                    reqBo.getLongitude() + "," + reqBo.getLatitude());
            activity.setDistance(apiDistance);
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activity.getUserUuid());
            if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                log.info("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},eHomeUserIm = {}", activity.getUserUuid(), eHomeUserIm);
                continue;
            }
            activity.setImId(eHomeUserIm.getImId());
            UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
            activity.setAvatarImages(userDetail1.getAvatarThumbnail());
            activity.setAge(userDetail1.getAge());
            activity.setSex(userDetail1.getSex());
            activity.setBirthday(userDetail1.getBirthday());
            activity.setUserName(userDetail1.getNickName());
        }
        return activityDtos;
    }

    /**
     * 活动人所在地
     *
     * @param activityReqVoDto 用户id，查询时间类型
     */
    @Override
    public List<ActivityDto> queryActivity(ActivityReqVoDto activityReqVoDto) {
        log.info("activityReqVo的参数{}", activityReqVoDto);
        List<ActivityDto> activityDtos = this.activityMapper.queryActivityList(activityReqVoDto);
        for (ActivityDto activity : activityDtos) {
            long apiDistance = GouldUtil.getApiDistance(activity.getLongitude() + "," + activity.getLatitude(),
                    activityReqVoDto.getLongitude() + "," + activityReqVoDto.getLatitude());
            activity.setDistance(apiDistance);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activity.getUserUuid());
            if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                log.info("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},UserImVo = {}", activity.getUserUuid(), eHomeUserIm);
                continue;
            }
            activity.setImId(eHomeUserIm.getImId());
            UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
            activity.setAvatarImages(userDetail1.getAvatarThumbnail());
            activity.setAge(userDetail1.getAge());
            activity.setSex(userDetail1.getSex());
            activity.setBirthday(userDetail1.getBirthday());
            activity.setUserName(userDetail1.getNickName());
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
        Activity activity = new Activity();
        //辅助相同属性
        BeanUtils.copyProperties(reqBo, activity);
        LocalDateTime now = LocalDateTime.now();
        //默认不是好友,新增属性
        //activity.setUserUuid(userAuth().getAccount());
        UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getAccount());
        activity.setUserUuid(userDetail1.getAccount());
        activity.setPhone(userDetail1.getPhone());
        activity.setUserName(userDetail1.getNickName());
        activity.setSex(userDetail1.getSex());
        activity.setAge(userDetail1.getAge());
        activity.setDeleted(true);
        activity.setIsUser(true);
        activity.setPublishTime(now);
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        //新增活动表
        return this.activityMapper.insert(activity);
    }


    /**
     * 查询自己的所有活动
     *
     * @param activityPageDto
     */
    @Override
    public List<ActivityDto> getUserActivityList(ActivityPageDto activityPageDto) {
        log.info("用户的uid:{}", activityPageDto);
        List<ActivityDto> activityDtos = this.activityMapper.selectgetUserActivityList(activityPageDto);
        for (ActivityDto activity : activityDtos) {
            if (activity.getUserUuid().equals(userAuth().getAccount())) {
                //这个是自己
                long apiDistance = GouldUtil.getApiDistance(activity.getLongitude() + "," + activity.getLatitude(),
                        activityPageDto.getLongitude() + "," + activityPageDto.getLatitude());
                activity.setDistance(apiDistance);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                if (userAuth() == null || userAuth().getImId() == null) {
                    log.info("userAuth() = {},userAuth().getImId() = {}", activity.getUserUuid(), userAuth());
                    continue;
                }
                activity.setImId(userAuth().getImId());
                UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
                activity.setAvatarImages(userDetail1.getAvatarThumbnail());
                activity.setBirthday(userDetail1.getBirthday());
                activity.setAge(userDetail1.getAge());
                activity.setSex(userDetail1.getSex());
                activity.setUserName(userDetail1.getNickName());
            }else {
                //这个不是自己
                long apiDistance = GouldUtil.getApiDistance(activity.getLongitude() + "," + activity.getLatitude(),
                        activityPageDto.getLongitude() + "," + activityPageDto.getLatitude());
                activity.setDistance(apiDistance);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activityPageDto.getId());
                if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                    log.info("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},eHomeUserIm = {}", activity.getUserUuid(), eHomeUserIm);
                    continue;
                }
                activity.setImId(eHomeUserIm.getImId());
                UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
                activity.setAvatarImages(userDetail1.getAvatarThumbnail());
                activity.setBirthday(userDetail1.getBirthday());
                activity.setAge(userDetail1.getAge());
                activity.setSex(userDetail1.getSex());
                activity.setUserName(userDetail1.getNickName());
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
        log.info("activityReqBo的参数:{}", activityReqBo);
        List<ActivityDto> activityDtos = this.activityMapper.pageListed(activityReqBo);
        for (ActivityDto activity : activityDtos) {
            long apiDistance = GouldUtil.getApiDistance(activity.getLongitude() + "," + activity.getLatitude(),
                    activityReqBo.getLongitude() + "," + activityReqBo.getLatitude());
            activity.setDistance(apiDistance);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activity.getUserUuid());
            if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                log.info("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},eHomeUserIm = {}", activity.getUserUuid(), eHomeUserIm);
                continue;
            }
            activity.setImId(eHomeUserIm.getImId());
            UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
            activity.setAvatarImages(userDetail.getAvatarThumbnail());
            activity.setBirthday(userDetail.getBirthday());
            activity.setAge(userDetail.getAge());
            activity.setSex(userDetail.getSex());
            activity.setUserName(userDetail.getNickName());
        }
        return activityDtos;
    }


    /**
     * 模糊查询活动
     *
     * @param likeActivity
     * @return activityReqDtos
     */
    @Override
    public List<ActivityReqDto> likeActivity(LikeActivityDto likeActivity) {
        log.info("likeActivity 的值:{}", likeActivity);
        UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getAccount());
        Activity activity = new Activity();
        if (userDetail1 == null) {
            activity.setDeleted(false);
        } else {
            activity.setDeleted(true);
        }
        activity.setUserName(userDetail1.getNickName());
        activity.setUserUuid(userDetail1.getAccount());
        activity.setAge(userDetail1.getAge());
        activity.setPhone(userDetail1.getPhone());
        QueryWrapper<Activity> Wrapper = new QueryWrapper<>();
        Wrapper.eq("user_uuid", userDetail1.getAccount());
        this.activityMapper.update(activity, Wrapper);
        return this.activityMapper.likeActivity(likeActivity);
    }

    /**
     * 根据活动id查询活动信息
     *
     * @param id
     * @return activityReqDto
     */
    @Override
    public ActivityReqDto getByIdActivity(Long id) {
        log.info("id 的值:{}", id);
        Activity activity = this.activityMapper.selectByIdActivity(id);
        ActivityReqDto activityReqDto = new ActivityReqDto();
        BeanUtils.copyProperties(activity, activityReqDto);
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
        if (userDetail == null || activity.getUserUuid() == null) {
            log.info("调用【iBaseUserInfoRpcService()】的【getUserDetail】获取【userDetail】为null，userDetail = {},activity.getUserUuid() = {}", userDetail, activity.getUserUuid());
            return activityReqDto;
        }
        activityReqDto.setSex(userDetail.getSex());
        activityReqDto.setPhone(userDetail.getPhone());
        activityReqDto.setUserName(userDetail.getNickName());
        return activityReqDto;
    }


    /**
     * ***************************************获取当前登录用户**********************************************************
     * *****************************************************************************************************
     */
    private LoginUser userAuth() {
        return ContextHolder.getContext().getLoginUser();
    }

}
