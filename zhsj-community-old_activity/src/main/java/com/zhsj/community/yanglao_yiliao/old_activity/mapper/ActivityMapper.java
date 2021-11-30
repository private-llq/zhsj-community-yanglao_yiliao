package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityReqVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


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
    @Select("select id,activity_type_name,activity_type_code from t_activity_type")
    List<ActivityFromDto> getActivityTyped();


    /**
     * 查找所有
     */
    List<ActivityDto> queryNearbyActivityList(ActivityReqBo reqBo);

    /**
     * 其他人查询自己的活动详情
     * @param id
     * @return
     */
    List<ActivityListDto> getActivityedge(@Param("id")Long id);



    Page<Activity>  getActivityedPage(@Param("id") Long id,Page page);


    List<ActivityDto> queryActivityList(ActivityReqVo activityReqVo);



    List<ActivityDto> pageListed(ActivityReqBo activityDto);



    List<ActivityDto> getActivityePagelist(ActivityPageDto activityPageDto);
}
