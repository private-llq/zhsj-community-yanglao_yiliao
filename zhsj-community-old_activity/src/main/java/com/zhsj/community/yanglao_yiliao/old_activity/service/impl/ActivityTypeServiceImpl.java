package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.ActivityTypeMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;
import com.zhsj.community.yanglao_yiliao.old_activity.service.ActivityTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chengl
 * @version 1.0
 * @Description: 查询附近活动类型列表
 * @date 2021/11/23 13:48
 */
@Slf4j
@Service
public class ActivityTypeServiceImpl  extends ServiceImpl<ActivityTypeMapper, ActivityType> implements ActivityTypeService {

    @Resource
    private ActivityTypeMapper activityTypeMapper;
    @Override
    public List<ActivityType> selectList() {
        List<ActivityType> list = activityTypeMapper.selectList(new LambdaQueryWrapper<ActivityType>()
                .orderByAsc(ActivityType::getActivityTypeName));
        return list;
    }


}
