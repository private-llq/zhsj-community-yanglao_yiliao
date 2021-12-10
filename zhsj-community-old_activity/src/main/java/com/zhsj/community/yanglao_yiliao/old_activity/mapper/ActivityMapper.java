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
     * @return
     */
    List<ActivityDto> queryActivityList(ActivityReqVoDto activityReqVoDto);

    /**
     * 分页查询所有活动
     *
     * @param activityReqBo
     * @return
     */
    List<ActivityDto> pageListed(ActivityReqBo activityReqBo);


    /**
     * 根据id查询自己的活动详情
     *
     * @param activityPageDto
     * @return
     */

    List<ActivityDto> selectgetUserActivityList(ActivityPageDto activityPageDto);

    /**
     * 根据id查询活动信息
     *
     * @return
     */
    @Select("SELECT * from  t_activity where id=#{id}")
    Activity selectByIdActivity(@Param("id") Long id);


    /**
     * 模糊查询活动信息
     *
     * @param likeActivity
     * @return
     */
    List<ActivityReqDto> likeActivity(@Param("likeActivity") LikeActivityDto likeActivity);

}
