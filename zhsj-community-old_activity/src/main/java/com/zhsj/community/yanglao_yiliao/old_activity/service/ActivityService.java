package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.community.yanglao_yiliao.old_activity.common.PageResult;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.*;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;

import java.util.HashSet;
import java.util.LinkedList;
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
     * 查询具体的活动
     */
    List<ActivityFrom> getactivit();


    /**
     * 新增活动
     * activityExplain:文字动态
     * location：位置名称
     * longitude：经度
     * latitude: 纬度
     * multipartFile：图片
     * voice:语音
     */
    int addActivity(addActivityFrom activityFrom);


    /**
     * 删除发布活动
     *
     */
    void deletedActivity(Long uid);

    /**
     * 查询附近的活动或者好友的活动
     *
     * @return
     */
    HashSet<LinkedList<UserLocation>> listActivities(UserLocationFrom userLocationFrom);


    /**
     * 点击头像查询查看活动、个人资料
     *
     * @return
     */
    Page<Activity> queryAlbumList(PageResult pageResult);



    /**
     * 更新用户信息
     *
     */
    int updateUserInfo(ActivityUpdateFrom activityUpdateFrom);

}
