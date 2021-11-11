package com.zhsj.community.yanglao_yiliao.old_activity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-11 15:10
 */
public interface UserLocationService {

    /**
     * 搜索附近的人
     * @param sex
     * @param distance
     * @return
     */
    List<UserLocationVo> queryNearUser(String sex, String distance);

    /**
     * 查询用户地理位置
     *
     * @param loginUser
     * @return
     */
    LoginUser queryByUserId(LoginUser loginUser);

    /**
     * 根据地理位置查询用户
     *
     * @param longitude
     * @param latitude
     * @return
     */
    List<UserLocationVo> queryUserFromLocation(Double longitude, Double latitude, Integer range);


    /**
     * 查询用户信息
     * @param queryWrapper
     */
    List<LoginUser> qqueryUserInfoList(QueryWrapper<LoginUser> queryWrapper);
}
