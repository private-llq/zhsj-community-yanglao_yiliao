package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.entity.UserDetail;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.constant.BaseConstant;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityReqDto;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypeDto;
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
    public List<ActivityType> selectList() {
        List<ActivityType> list = activityTypeMapper.selectList(new LambdaQueryWrapper<ActivityType>()
                .orderByAsc(ActivityType::getActivityTypeName));
        return list;
    }

    /**
     * 新增活动类型
     *
     * @param activityTypeDto
     */
    @Override
    public int activityType(ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDt的值:{}", activityTypeDto);
        ActivityType type = new ActivityType();
        LocalDateTime now = LocalDateTime.now();
        type.setActivityTypeCode(activityTypeDto.getActivityTypeCode());
        type.setActivityTypeName(activityTypeDto.getActivityTypeName());
        type.setDeleted(true);
        type.setCreateTime(now);
        type.setUpdateTime(now);
        return this.activityTypeMapper.insert(type);

    }

    /**
     * 修改活动类型
     *
     * @param activityTypeDto
     */
    @Override
    public int updateActivityType(ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDt的值:{}", activityTypeDto);
        ActivityType type = new ActivityType();
        type.setActivityTypeCode(activityTypeDto.getActivityTypeCode());
        type.setActivityTypeName(activityTypeDto.getActivityTypeName());
        QueryWrapper<ActivityType> Wrapper = new QueryWrapper<>();
        Wrapper.eq("activity_type_code", activityTypeDto.getActivityTypeCode());
        this.activityTypeMapper.selectOne(Wrapper);
        return this.activityTypeMapper.update(type, Wrapper);
    }

    /**
     * 删除活动类型
     *
     * @param activityTypeCode
     */
    @Override
    public void deleteActivityType(String activityTypeCode) {
        log.info("activityTypeCode:{}", activityTypeCode);
        this.activityTypeMapper.deleteActivityType(activityTypeCode);
    }

    /**
     * 查询所有的活动
     *
     * @return
     */
    @Override
    public List<ActivityReqDto> selectActivityList() {
        List<ActivityReqDto> activityReqDtos = this.activityMapper.selectActivityList();
        for (ActivityReqDto activity : activityReqDtos) {
            UserDetail userDetail = this.iBaseUserInfoRpcService.getUserDetail(activity.getUserUuid());
            activity.setPhone(userDetail.getPhone());
            activity.setAge(userDetail.getAge());
            activity.setSex(userDetail.getSex());
        }
        return activityReqDtos;
    }


}