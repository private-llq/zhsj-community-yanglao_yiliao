package com.zhsj.community.yanglao_yiliao.old_activity.service;

import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.model.Activity;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:03
 */
public interface ActivityService {
    /**
     * 查询具体的活动
     * @return
     */
    List<Activity> getType(LoginUser loginUser);
}
