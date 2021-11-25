package com.zhsj.community.yanglao_yiliao.old_activity.service;

import com.zhsj.community.yanglao_yiliao.old_activity.po.ActivityType;

import java.util.List;

/**
 * @author chengl
 * @version 1.0
 * @Description: 查询活动类型列表
 * @date 2021/11/23 13:48
 */

public interface ActivityTypeService {

  List<ActivityType> selectList();

}
