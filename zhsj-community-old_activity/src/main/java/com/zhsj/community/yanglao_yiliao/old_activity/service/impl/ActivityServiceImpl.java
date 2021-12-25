package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.base.api.vo.PageVO;
import com.zhsj.base.api.vo.UserImVo;
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
        LocalDateTime now = LocalDateTime.now();
        List<ActivityDto> activityDtos = this.activityMapper.queryNearbyActivityList(reqBo);
        for (ActivityDto activity : activityDtos) {
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    reqBo.getLatitude() + "," + reqBo.getLongitude());
            activity.setDistance(apiDistance / 1000);
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activity.getUserUuid());
            if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                log.error("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},UserImVo = {}", activity.getUserUuid(), eHomeUserIm);
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
     * 获取附近活动
     *
     * @param activityReqVoDto 用户id，查询时间类型
     */
    @Override
    public List<ActivityDto> queryActivity(ActivityReqVoDto activityReqVoDto) {
        log.info("activityReqVo的参数{}", activityReqVoDto);
        List<ActivityDto> activityDtos = this.activityMapper.queryActivityList(activityReqVoDto);
        for (ActivityDto activity : activityDtos) {
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    activityReqVoDto.getLatitude() + "," + activityReqVoDto.getLongitude());
            activity.setDistance(apiDistance / 1000);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activity.getUserUuid());
            if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                log.error("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},UserImVo = {}", activity.getUserUuid(), eHomeUserIm);
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
        UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(userAuth().getId());
        Activity activity = new Activity();
        //辅助相同属性
        BeanUtils.copyProperties(reqBo, activity);
        LocalDateTime now = LocalDateTime.now();
        //默认不是好友  后期能查询 再调整 2021-11-23
        activity.setUserUuid(userAuth().getAccount());
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
        log.info("用户的uid:{}", activityPageDto);
        List<ActivityDto> activityDtos = this.activityMapper.selectgetUserActivityList(activityPageDto);
        for (ActivityDto activity : activityDtos) {
            if (activity.getUserUuid().equals(userAuth().getAccount())) {
                //这个是自己
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        activityPageDto.getLatitude() + "," + activityPageDto.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                if (userAuth() == null || userAuth().getImId() == null) {
                    log.error("调用【userAuth()】的【getEHomeUserIm】获取【E到家用户imid】为null，userAuth() = {},userAuth().getImId() = {}", activity.getUserUuid(), userAuth());
                }
                activity.setImId(userAuth().getImId());
                UserDetail userDetail1 = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
                activity.setAvatarImages(userDetail1.getAvatarThumbnail());
                activity.setBirthday(userDetail1.getBirthday());
                activity.setAge(userDetail1.getAge());
                activity.setSex(userDetail1.getSex());
                activity.setUserName(userDetail1.getNickName());
            }
            if (!activity.getUserUuid().equals(userAuth().getAccount())) {
                //这个不是自己
                long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                        activityPageDto.getLatitude() + "," + activityPageDto.getLongitude());
                activity.setDistance(apiDistance / 1000);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime publishTime = activity.getPublishTime();
                //相差的分钟数
                long minutes = Duration.between(publishTime, now).toMinutes();
                activity.setPublishTimed(minutes);
                UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activityPageDto.getId());
                if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                    log.error("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},UserImVo = {}", activity.getUserUuid(), eHomeUserIm);
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
            long apiDistance = (long) GouldUtil.getDistance(activity.getLatitude() + "," + activity.getLongitude(),
                    activityReqBo.getLatitude() + "," + activityReqBo.getLongitude());
            activity.setDistance(apiDistance / 1000);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime publishTime = activity.getPublishTime();
            //相差的分钟数
            long minutes = Duration.between(publishTime, now).toMinutes();
            activity.setPublishTimed(minutes);
            UserImVo eHomeUserIm = this.iBaseUserInfoRpcService.getEHomeUserIm(activity.getUserUuid());
            if (eHomeUserIm == null || eHomeUserIm.getImId() == null) {
                log.error("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，activity.getUserUuid() = {},UserImVo = {}", activity.getUserUuid(), eHomeUserIm);
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
        //假如没有电话和昵称，调别人的接口
        List<ActivityReqDto> list = new ArrayList<>();
        if (!likeActivity.getPublishTime().isEmpty() || !likeActivity.getActivityTypeName().isEmpty()) {
            List<ActivityReqDto> activityReqDtos = this.activityMapper.likeActivity(likeActivity);
            for (ActivityReqDto a : activityReqDtos) {
                UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(a.getUserUuid());
                a.setSex(userDetail.getSex());
                a.setPhone(userDetail.getPhone());
                a.setUserName(userDetail.getNickName());
                a.setAge(userDetail.getAge());
            }
            list.addAll(activityReqDtos);
        }
        if (!likeActivity.getUserName().isEmpty() || !likeActivity.getPhone().isEmpty()) {
            PageVO<UserDetail> userDetailPageVO = this.iBaseUserInfoRpcService.queryUser(likeActivity.getUserName(), likeActivity.getPhone(), likeActivity.getPage(), likeActivity.getData());
            for (UserDetail d : userDetailPageVO.getData()) {
                List<ActivityReqDto> activity = this.activityMapper.selectByIdActivityed(d.getAccount());
                if (activity == null) {
                    System.out.println("不存在:" + activity);
                    continue;
                }
                ActivityReqDto activityReqDto = new ActivityReqDto();
                BeanUtils.copyProperties(activity, activityReqDto);
                activityReqDto.setPhone(d.getPhone());
                activityReqDto.setAge(d.getAge());
                activityReqDto.setSex(d.getSex());
                activityReqDto.setUserName(d.getNickName());
                list.addAll(activity);
            }
        }
        if (!likeActivity.getUserName().isEmpty() || !likeActivity.getPhone().isEmpty() || !likeActivity.getUserName().isEmpty() || !likeActivity.getPhone().isEmpty()) {
            List<ActivityReqDto> objects = new ArrayList<>();
            PageVO<UserDetail> userDetailPageVO = this.iBaseUserInfoRpcService.queryUser(likeActivity.getUserName(), likeActivity.getPhone(), likeActivity.getPage(), likeActivity.getData());
            for (UserDetail userDetail : userDetailPageVO.getData()) {
                List<ActivityReqDto> list1 = this.activityMapper.selectByIdActivityed(userDetail.getAccount());
                objects.addAll(list1);
            }
            List<ActivityReqDto> activityReqDtos = this.activityMapper.likeActivity(likeActivity);
            objects.addAll(activityReqDtos);
            for (ActivityReqDto activityReqDto : objects) {
                UserDetail d = this.iBaseUserInfoRpcService.getUserDetail(activityReqDto.getUserUuid());
                activityReqDto.setPhone(d.getPhone());
                activityReqDto.setAge(d.getAge());
                activityReqDto.setSex(d.getSex());
                activityReqDto.setUserName(d.getNickName());
            }
            list.addAll(objects);
        }
        return list;
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
