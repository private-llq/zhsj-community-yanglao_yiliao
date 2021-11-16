package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.UserLocationFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.UserLocationMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    @Resource
    private  UserLocationService userLocationService;

//    @Resource
//    private MongoTemplate mongoTemplate;


    /**
     * 活动搜索
     */
    @Override
    public List<UserLocationVo> queryNearUser(UserLocationFrom userLocationFrom) {
        log.info("传过来的参数：", userLocationFrom);
        //查询当前用户的地理位置
        UserLocationVo userLocationVo = this.userLocationMapper.queryByUserId(userAuth());
        //获取经纬度
        Double latitude = userLocationVo.getLatitude();
        Double longitude = userLocationVo.getLongitude();
        //查询附近的好友
        List<UserLocationVo> userLocationVos = this.userLocationService.queryUserFromLocation(longitude, latitude, Integer.valueOf(userLocationFrom.getBeatadistancefrom()));
        if (CollectionUtils.isEmpty(userLocationVos)) {
            return Collections.emptyList();
        }

        List<Long> userIds = new ArrayList<>();
        for (UserLocationVo locationVo : userLocationVos) {
            userIds.add(locationVo.getUserId());
        }

        //获取用户的全部信息
        LoginUser user = ContextHolder.getContext().getLoginUser();
        List<UserLocationVo> result = new ArrayList<>();
        for (UserLocationVo locationVo : userLocationVos) {
            //排除自己
            if (locationVo.getUserId().longValue() == user.getId().longValue()) {
                continue;
            }
            for (UserLocationVo userInfo : result) {
                if (locationVo.getUserId().longValue() == userInfo.getUserId().longValue()) {
                           //添加好友.........





                }

            }
        }
        return userLocationVos;
    }


    @Override
    public LoginUser queryByUserId(UserLocationFrom loginUser) {
        return ContextHolder.getContext().getLoginUser();
    }

    /**
     * 地图
     */
    @Override
    public List<UserLocationVo> queryUserFromLocation(Double longitude, Double latitude, Integer range) {
        // 根据传入的坐标，进行确定中心点
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(longitude, latitude);
        // 画圈的半径
        Distance distance = new Distance(range / 1000, Metrics.KILOMETERS);
        // 画了一个圆圈
        Circle circle = new Circle(geoJsonPoint, distance);
        Query query = Query.query(Criteria.where("location").withinSphere(circle));
//        List<UserLocation> userLocations = this.mongoTemplate.find(query, UserLocation.class);
//        return UserLocationVo.formatToList(userLocations);\
          return null;
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
