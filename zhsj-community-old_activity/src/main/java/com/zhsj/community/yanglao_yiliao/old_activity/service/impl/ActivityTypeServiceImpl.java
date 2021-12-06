package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypeDto;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityTypeMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityTypeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private ActivityTypeMapper activityTypeMapper;

    /**
     * 查询活动类型
     *
     * @return
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
    public int ActivityType(ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDt的值{}", activityTypeDto);
            ActivityType type = new ActivityType();
            LocalDateTime now = LocalDateTime.now();
            type.setActivityTypeCode(activityTypeDto.getActivityTypeCode());
            type.setActivityTypeName(activityTypeDto.getActivityTypeName());
            type.setDeleted(true);
            type.setCreateTime(now);
            type.setUpdateTime(now);
          return   this.activityTypeMapper.insert(type);

    }

    /**
     * 修改活动类型
     * @param activityTypeDto
     * @return
     */
    @Override
    public int updateActivityType(ActivityTypeDto activityTypeDto) {
        log.info("activityTypeDt的值{}", activityTypeDto);
        ActivityType type = new ActivityType();
        type.setActivityTypeCode(activityTypeDto.getActivityTypeCode());
        type.setActivityTypeName(activityTypeDto.getActivityTypeName());
        QueryWrapper<ActivityType> Wrapper = new QueryWrapper<>();
        Wrapper.eq("activity_type_code",activityTypeDto.getActivityTypeCode());
        this.activityTypeMapper.selectOne(Wrapper);
        return this.activityTypeMapper.update(type,Wrapper);
    }
}