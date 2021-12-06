package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypeDto;
import com.zhsj.community.yanglao_yiliao.old_activity.model.ActivityType;

import java.util.List;

/**
 * @author chengl
 * @version 1.0
 * @Description: 查询活动类型列表
 * @date 2021/11/23 13:48
 */

public interface ActivityTypeService {
    /**
     * 查询活动类型
     * @return
     */
    List<ActivityType> selectList();

    /**
     * 新增活动类型
     */
    int ActivityType(ActivityTypeDto activityTypeDto);

    /**
     * 修改活动类型
     * @return
     */
    int updateActivityType(ActivityTypeDto activityTypeDto);
}
