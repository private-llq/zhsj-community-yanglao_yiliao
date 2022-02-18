package com.zhsj.community.yanglao_yiliao.old_activity.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
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
     * 查找所有
     */
    List<ActivityDto> queryNearbyActivityList(ActivityReqBo reqBo);


    /**
     * 获取附近活动
     *
     * @param activityReqVoDto
     * @return ActivityDto
     */
    List<ActivityDto> queryActivityList(ActivityReqVoDto activityReqVoDto);

    /**
     * 分页查询所有活动
     *
     * @param activityReqBo
     * @return ActivityDto
     */
    List<ActivityDto> pageListed(ActivityReqBo activityReqBo);


    /**
     * 根据id查询自己的活动详情
     *
     * @param activityPageDto
     * @return ActivityDto
     */

    List<ActivityDto> selectgetUserActivityList(ActivityPageDto activityPageDto);

    /**
     * 根据id查询活动信息
     *
     * @return Activity
     */
    @Select("SELECT * from  t_activity where id=#{id} and deleted = 1")
    Activity selectByIdActivity(@Param("id") Long id);


    /**
     * 模糊查询活动信息
     *
     * @param likeActivity
     * @return ActivityReqDto
     */
    List<ActivityReqDto> likeActivity(@Param("likeActivity") LikeActivityDto likeActivity);

    /**
     * 大后台查询所有的活动
     *
     * @return ActivityReqDto
     */
    @Select("SELECT * from  t_activity as t where deleted = 1 ORDER BY t.publish_time DESC")
    List<ActivityReqDto> selectActivityList();

}
