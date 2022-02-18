package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityReqDto;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypeDto;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityTypedDto;


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
     *
     * @return ActivityType
     */
    List<ActivityTypedDto> selectList();

    /**
     * 新增活动类型
     *
     * @param activityTypeDto
     * @return int
     */
    boolean activityType(ActivityTypeDto activityTypeDto);

    /**
     * 修改活动类型
     *
     * @param activityTypeDto
     * @return int
     */
    boolean updateActivityType(ActivityTypeDto activityTypeDto);

    /***
     * 删除活动类型
     * @param id
     */
    void deleteActivityType(String id);

    /**
     * 大后台查询所有的活动
     */
    List<ActivityReqDto> selectActivityList();
}
