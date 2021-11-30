package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityReqVo;


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
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     */
    List<ActivityDto> queryActivityList(ActivityReqBo reqBo);

    /**
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     * @param activityReqVo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     */
    List<ActivityDto> queryActivity(ActivityReqVo activityReqVo);

    /**
     * @Description: 删除
     * @author: liulq
     * @date 2021/11/23 10:43
     * @Param:
     * @return:
     */
    void delete(Long id);
    /**
     * @Description: 保存
     * @author: liulq
     * @date 2021/11/23 10:43
     *
     */
    int publishActivity(ActivitySaveReqBo reqBo);


    /**
     * 查询具体的活动
     *
     */
     List<ActivityFromDto> getactivit();


    /**
     * 查询自己的所有活动
     * @param
     * @return
     */
    IPage<Activity> getActivityList(Page<Activity> page);

    /**
    *@Description:
    *@Param:
    *@return:
    *@Author: liulq
    *@date: 2021-11-29
    */
    List<ActivityDto> pageListed(ActivityReqBo activityDto);


    /**
    *@Description:
    *@Param:
    *@return:
    *@Author: liulq
    *@date: 2021-11-29
    */
    List<ActivityDto> getActivityePagelist(ActivityPageDto activityPageDto);
}
