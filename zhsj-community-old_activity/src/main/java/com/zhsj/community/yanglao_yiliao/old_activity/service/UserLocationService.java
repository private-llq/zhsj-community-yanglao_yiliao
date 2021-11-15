package com.zhsj.community.yanglao_yiliao.old_activity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description:
 * @create: 2021-11-11 15:10
 */
public interface UserLocationService {

    /**
     * 搜索附近的人
     */
    List<UserLocationVo> queryNearUser(String sex, String distance);

    /**
     * 查询用户地理位置
     *
     */
    LoginUser queryByUserId(LoginUser loginUser);

    /**
     * 根据地理位置查询用户
     *
     */
    List<UserLocationVo> queryUserFromLocation(Double longitude, Double latitude, Integer range);


    /**
     * 查询用户信息
     */
    List<LoginUser> queryUserInfoList(QueryWrapper<LoginUser> queryWrapper);
}
