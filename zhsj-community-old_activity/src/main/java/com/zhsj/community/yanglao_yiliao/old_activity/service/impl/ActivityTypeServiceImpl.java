package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityReqDto;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypeDto;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypedDto;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityTypeMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chengl
 * @version 1.0
 * @Description: 查询附近活动类型列表
 * @date 2021/11/23 13:48
 */
@Slf4j
@Service
public class ActivityTypeServiceImpl extends ServiceImpl<ActivityTypeMapper, ActivityType> implements ActivityTypeService {

    @Autowired
    private ActivityTypeMapper activityTypeMapper;

    @DubboReference(version = BaseConstant.Rpc.VERSION, group = BaseConstant.Rpc.Group.GROUP_BASE_USER)
    private IBaseUserInfoRpcService iBaseUserInfoRpcService;

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 查询活动类型
     */
    @Override
    public List<ActivityTypedDto> selectList() {
        List<ActivityTypedDto> list = activityTypeMapper.selectListctivityType();
        return list;
    }

    /**
     * 新增活动类型
     *
     * @param activityTypeDto
     */
    @Override
    public boolean activityType(ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDt的值:{}", activityTypeDto);
        //判断是否有重复的ActivityTypeName，有返回提示，没有新增
        List<ActivityType> activityTypes = this.activityTypeMapper.selectList(null);
        for (ActivityType activityType : activityTypes) {
            if (activityType.getActivityTypeName().equals(activityTypeDto.getActivityTypeName())) {
                return false;
            }
        }
        this.activityTypeMapper.addByIdActivityType(activityTypeDto.getActivityTypeCode());
        ActivityType type = new ActivityType();
        LocalDateTime now = LocalDateTime.now();
        type.setActivityTypeCode(activityTypeDto.getActivityTypeCode());
        type.setActivityTypeName(activityTypeDto.getActivityTypeName());
        type.setDeleted(true);
        type.setCreateTime(now);
        type.setUpdateTime(now);
        this.activityTypeMapper.insert(type);
        return true;
    }

    /**
     * 修改活动类型
     *
     * @param activityTypeDto
     */
    @Override
    public boolean updateActivityType(ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDt的值:{}", activityTypeDto);
        //先删除，判断是否有重复的,如果没有插入，有返回提示
        this.activityTypeMapper.deleteByIdactivityType(activityTypeDto.getId());
        List<ActivityType> activityTypes = this.activityTypeMapper.selectList(null);
        for (ActivityType activityType : activityTypes) {
            if (activityType.getActivityTypeName().equals(activityTypeDto.getActivityTypeName())) {
                return false;
            }
        }
        ActivityType activityType = this.activityTypeMapper.selectOne(new QueryWrapper<ActivityType>().eq("activity_type_code", activityTypeDto.getActivityTypeCode()));
        //没有重复的，activity_type_code不用+1
        if (activityType!=null){
            this.activityTypeMapper.addByIdActivityType(activityTypeDto.getActivityTypeCode());
        }
        ActivityType type = new ActivityType();
        LocalDateTime now = LocalDateTime.now();
        type.setActivityTypeCode(activityTypeDto.getActivityTypeCode());
        type.setActivityTypeName(activityTypeDto.getActivityTypeName());
        type.setDeleted(true);
        type.setCreateTime(now);
        type.setUpdateTime(now);
        this.activityTypeMapper.insert(type);
        return true;
    }

    /**
     * 删除活动类型
     *
     * @param id
     */
    @Override
    public void deleteActivityType(String id) {
        log.info("activityTypeCode:{}", id);
        this.activityTypeMapper.deleteById(id);
    }

    /**
     * 查询所有的活动
     *
     * @return activityReqDtos
     */
    @Override
    public List<ActivityReqDto> selectActivityList() {
        List<ActivityReqDto> activityReqDtos = this.activityMapper.selectActivityList();
        for (ActivityReqDto activity : activityReqDtos) {
            UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
            if (userDetail == null || activity.getUserUuid() == null) {
                log.info("调用【iBaseUserInfoRpcService()】的【getUserDetail】获取【userDetail】为null，userDetail= {},activity.getUserUuid() = {}", userDetail, activity.getUserUuid());
                return activityReqDtos;
            }
            activity.setPhone(userDetail.getPhone());
            activity.setAge(userDetail.getAge());
            activity.setSex(userDetail.getSex());
            activity.setUserName(userDetail.getNickName());
        }
        return activityReqDtos;
    }


}