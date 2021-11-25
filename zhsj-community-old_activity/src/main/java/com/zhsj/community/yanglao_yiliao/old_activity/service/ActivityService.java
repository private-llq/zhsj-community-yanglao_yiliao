package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.zhsj.community.yanglao_yiliao.old_activity.dto.*;


import java.util.List;



/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 活动的业务接口
 * @create: 2021-11-10 17:03
 */
public interface ActivityService {


    /**
     * @description 查询附近活动列表
     * @author liulq
     * @date 2021/11/23 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     */
    List<ActivityDto> queryActivityList(ActivityReqBo reqBo);

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



}
