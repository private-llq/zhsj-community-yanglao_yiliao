package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.util.SexEnum;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.UserLocationMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-11 15:10
 */
@Service
@Slf4j
public class UserLocationServiceImpl implements UserLocationService {

    @Resource
    private UserLocationMapper userLocationMapper;


    /**
     * 活动搜索
     */
    @Override
    public List<UserLocationVo> queryNearUser(String sex, String distance) {
        log.info("传过来的sex{}和距离{}",sex,distance);
        //查询当前用户的地理位置
        UserLocationVo userLocationVo = this.userLocationMapper.queryByUserId(userAuth());
        //获取经纬度
        Double latitude = userLocationVo.getLatitude();
        Double longitude = userLocationVo.getLongitude();
        //查询附近的好友
        List<UserLocationVo> userLocationVos = this.userLocationMapper.queryUserFromLocation(longitude, latitude, Integer.valueOf(distance));
        if (CollectionUtils.isEmpty(userLocationVos)){
            return Collections.emptyList();
        }
        List<Long> userIds = new ArrayList<>();
        for (UserLocationVo locationVo : userLocationVos) {
            userIds.add(locationVo.getUserId());
        }
        //判断是女生还是男生
        QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", userIds);
        if (StringUtils.equalsIgnoreCase(sex, "man")) {
            queryWrapper.eq("sex", SexEnum.MAN);
        } else if (StringUtils.equalsIgnoreCase(sex, "woman")) {
            queryWrapper.eq("sex", SexEnum.WOMAN);
        }
        //获取用户的全部信息
        List<LoginUser> loginUsers = this.userLocationMapper.qqueryUserInfoList(queryWrapper);
        List<UserLocationVo> result = new ArrayList<>();
        for (UserLocationVo locationVo : userLocationVos) {
            //排除自己
            if (locationVo.getUserId().longValue() == userAuth().getId().longValue()) {
//                continue;
            }
        }
//       这个写添加好友,等后面再写吧

        return userLocationVos;
    }

    @Override
    public LoginUser queryByUserId(LoginUser loginUser) {
        return ContextHolder.getContext().getLoginUser();
    }

    /**
     * 地图
     */
    @Override
    public List<UserLocationVo> queryUserFromLocation(Double longitude, Double latitude, Integer range) {
//        // 根据传入的坐标，进行确定中心点
//        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(longitude, latitude);
//        // 画圈的半径
//        Distance distance = new Distance(range / 1000, Metrics.KILOMETERS);
//        // 画了一个圆圈
//        Circle circle = new Circle(geoJsonPoint, distance);
//        Query query = Query.query(Criteria.where("location").withinSphere(circle));
//        List<UserLocation> userLocations = this.mongoTemplate.find(query, UserLocation.class);
//        return UserLocationVo.formatToList(userLocations);
         return  null;
    }

    /**
     * 查询用户信息
     */
    @Override
    public List queryUserInfoList(QueryWrapper queryWrapper) {
        return this.userLocationMapper.selectList(queryWrapper);
    }


    /**
     *  ***************************************获取当前登录用户**********************************************************
     *       *****************************************************************************************************
     */
    private LoginUser userAuth() {
        return ContextHolder.getContext().getLoginUser();
    }

}
