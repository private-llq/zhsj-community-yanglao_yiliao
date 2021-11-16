package com.zhsj.community.yanglao_yiliao.old_activity.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description:
 * @create: 2021-11-11 15:10
 */
@Repository
public interface UserLocationMapper extends BaseMapper<UserLocation> {
    /**
     * 查询用户地理位置
     *
     */
    UserLocationVo queryByUserId(LoginUser loginUser);

    /**
     * 根据地理位置查询用户
     *
     */
    List<UserLocationVo> queryUserFromLocation(Double longitude, Double latitude, Integer range);

    /**
     * 查询用户信息
     */
    List<LoginUser> qqueryUserInfoList(QueryWrapper<LoginUser> queryWrapper);
}
