package com.zhsj.community.yanglao_yiliao.old_activity.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhsj.community.yanglao_yiliao.old_activity.common.PageResult;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.ActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.addActivityFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.ActivityVo;

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
    void deletedActivity(Long id);

    /**
     * 查询附近的活动或者好友的活动
     *
     */
    List<Activity> listActivities(addActivityFrom addActivityFrom);


    /**
     * 点击头像查询查看活动、个人资料
     *
     */
    IPage<Activity> queryAlbumList(PageResult pageResult);



    /**
     * 更新用户信息
     * @return
     */
    int updateUserInfo(ActivityVo activityVo);

}
