package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;


import java.util.List;


/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 活动的业务接口
 * @create: 2021-11-10 17:03
 */
public interface ActivityService extends IService<Activity> {


    /**
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     */
    List<ActivityDto> queryActivityList(ActivityReqBo reqBo);

    /**
     * @param activityReqVoDto 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     */
    List<ActivityDto> queryActivity(ActivityReqVoDto activityReqVoDto);

    /**
     * @Description: 删除
     * @author: liulq
     * @date 2021/11/23 10:43
     * @Param: id
     * @return: void
     */
    void delete(Long id);

    /**
     * @Description: 保存
     * @author: liulq
     * @date 2021/11/23 10:43
     */
    int publishActivity(ActivitySaveReqBo reqBo);


    /**
     * 查询自己的所有活动
     *
     * @param activityPageDto
     * @return ActivityDto
     */
    List<ActivityDto> getUserActivityList(ActivityPageDto activityPageDto);

    /**
     * @Description: 分页查询所有活动
     * @Param: activityReqBo
     * @return: ActivityDto
     * @Author: liulq
     * @date: 2021-11-29
     */
    List<ActivityDto> pageListed(ActivityReqBo activityReqBo);


    /**
     * 大后台模糊查询活动
     *
     * @param likeActivity
     * @return ActivityReqDto
     */
    List<ActivityReqDto> likeActivity(LikeActivityDto likeActivity);

    /**
     * 根据id查询活动信息
     *
     * @param id
     * @return ActivityReqDto
     */
    ActivityReqDto getByIdActivity(Long id);


}
