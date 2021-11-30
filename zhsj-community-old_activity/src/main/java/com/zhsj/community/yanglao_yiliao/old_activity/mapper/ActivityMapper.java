package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityDto;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityReqBo;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;

//import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityFromDto;


/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description: 活动的mapper层
 * @create: 2021-11-10 17:02
 */
@Repository
public interface ActivityMapper extends BaseMapper<Activity> {
    /**
     * 查询活动类型
     *
     */
//    @Select("select id,activity_type_name,activity_type_code from t_activity_type")
//    List<ActivityFromDto> getActivityTyped();


    /**
     * 查找所有
     */
    List<ActivityDto> queryNearbyActivityList(ActivityReqBo reqBo);

}
