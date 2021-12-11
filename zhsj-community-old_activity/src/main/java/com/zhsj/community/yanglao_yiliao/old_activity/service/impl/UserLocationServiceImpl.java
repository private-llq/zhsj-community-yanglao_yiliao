package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.UserLocationMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 距离的实现类
 * @create: 2021-11-11 15:10
 */
@Service
@Slf4j
public class UserLocationServiceImpl implements UserLocationService {


    @Autowired
    private UserLocationMapper userLocationMapper;

    @Autowired
    private UserLocationService userLocationService;


    /**
     * 最大获取数量：20
     */
    private static final int MAX_LIMIT = 20;
    /**
     * 默认返回 2km
     */
    private static final Double DEFAULT_RADIUS = 1.0;
    private static final Metric RADIUS_METRIC = Metrics.KILOMETERS;

    private final RedisTemplate<String, String> redisTemplate;
    private static final String CACHE_KEY = "GEO_TEST";

    public UserLocationServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * ***************************************获取当前登录用户**********************************************************
     * *****************************************************************************************************
     */
    private LoginUser userAuth() {
        return ContextHolder.getContext().getLoginUser();
    }


    /**
     * 验证坐标是否合法，经度: -180~180、纬度：-90~90
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 是否合法
     */
    private Boolean invalidCoordinates(Double latitude, Double longitude) {
        log.info("经纬度{}{}", latitude, longitude);
        return latitude == null ||
                longitude == null ||
                Math.abs(latitude) > 90 ||
                Math.abs(longitude) > 180;
    }


    @Override
    public Boolean saveUserLocation(String member, Double latitude, Double longitude) {
        log.info("距离和经纬度{}{}{}", member, latitude, longitude);
        if (member == null || member.isEmpty() || invalidCoordinates(latitude, longitude)) {
            return false;
        }
        // 封装坐标
        Point point = new Point(longitude, latitude);
        Long remove = redisTemplate.opsForZSet().remove(CACHE_KEY, member);
        log.info("清除用户：id=" + member + "过期位置信息, 状态：" + remove);
        Long status = 0L;
        //Add操作
        if (remove != null) {
            status = redisTemplate.opsForGeo().add(CACHE_KEY, point, member);
        }
        return status != null && status > 0;
    }


    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(String member, Double distance, Metric metric, int limit) {
        log.info("距离和经纬度和限制{}{}{}{}", member, distance, metric, limit);
        if (member == null || member.isEmpty() || distance == null || distance <= 0 || metric == null || limit <= 0) {
            return null;
        }
        // radius命令参数：距离、坐标、上限（避免附近用户过多）、升序排序（距离近靠前）
        RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .limit(limit)
                .sortAscending();
        // geoRadius操作，返回结果暂不处理
        return redisTemplate
                .opsForGeo()
                .radius(CACHE_KEY, member, new Distance(distance, metric), commandArgs);
    }


    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(Double latitude, Double longitude, Double distance, Metric metric, int limit) {
        log.info("经纬度和距离{}{}{}{}", latitude, longitude, distance, metric, limit);
        if (invalidCoordinates(latitude, longitude) || distance == null || distance <= 0 || metric == null || limit <= 0) {
            return null;
        }
        RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .limit(limit)
                .sortAscending();
        Point center = new Point(longitude, latitude);
        Distance radius = new Distance(distance, metric);
        return redisTemplate
                .opsForGeo()
                .radius(CACHE_KEY, new Circle(center, radius), commandArgs);

    }


    @Override
    public Double getDistanceBetween(Integer userId, Integer otherUserId) {
        log.info("userid{}{}", userId, otherUserId);
        if (userId == null || otherUserId == null || userId <= 0 || otherUserId <= 0) {
            throw new IllegalArgumentException();
        }
        //获取用户信息
//LoginUser user = ContextHolder.getContext().getLoginUser();
        Distance distance = redisTemplate
                .opsForGeo()
                .distance(CACHE_KEY, userId.toString(), otherUserId.toString(), Metrics.NEUTRAL);

        return distance == null ? null : distance.getValue();
    }

    @Override
    public Map<String, Double> getUserLocation(Integer userId) {
        log.info("userid是{}", userId);
        //获取用户信息
//        LoginUser user = ContextHolder.getContext().getLoginUser();
        List<Point> points = redisTemplate.opsForGeo().position(CACHE_KEY, userAuth().getId().toString());
        if (points == null) {
            return null;
        }
        Map<String, Double> result = new HashMap<>(8);
        result.put("latitude", points.get(0).getY());
        result.put("longitude", points.get(0).getX());
        return result;
    }

    @Override
    public Boolean deleteUserLocation(Integer userId) {
        log.info("userid的值是{}", userId);
        if (userId == null || userId < 0) {
            return false;
        }
        Long status = redisTemplate.opsForZSet().remove(CACHE_KEY, userId.toString());
        return status != null && status > 0;
    }

    @Override
    public List<UserLocationVo> listNearbyUsers(Double latitude, Double longitude, Integer limit) {
        log.info("经纬度和限制的值{}{}{}", latitude, longitude, limit);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.userLocationService.listLocationsInRadius(latitude,
                longitude, DEFAULT_RADIUS, RADIUS_METRIC, limit > MAX_LIMIT ? MAX_LIMIT : limit);
        return geoResultToVO(results);
    }

    @Override
    public List<UserLocationVo> listNearbyUsersed(Long userId, Integer limit) {
        log.info("userid和limit的值{}{}", userId, limit);
        if (userId == null || limit == null || limit <= 0) {
            throw new IllegalArgumentException();
        }
        String member = userId.toString();
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.userLocationService.listLocationsInRadius(member,
                DEFAULT_RADIUS, RADIUS_METRIC, limit > MAX_LIMIT ? MAX_LIMIT : limit);
        return geoResultToVO(results);
    }

    @Override
    public UserLocation listUserLocation(Long uId) {
        log.info("用户id的值{}", uId);
        UserLocation userLocation = this.userLocationMapper.selectOne(new QueryWrapper<UserLocation>().select().eq("u_id", uId));
        return userLocation;
    }


    /**
     * 将redis geo的结果转换成页面显示结果
     *
     * @param results redis geo结果集
     * @return 附近用户VO集合
     */
    private List<UserLocationVo> geoResultToVO(GeoResults<RedisGeoCommands.GeoLocation<String>> results) {
        log.info("结果集{}", results);
        if (results == null) {
            return null;
        }
        List<UserLocationVo> nearbyUsers = new ArrayList<>();
        results.forEach((result) -> {
            double latitude = result.getContent().getPoint().getY();
            double longitude = result.getContent().getPoint().getX();
            LoginUser user = ContextHolder.getContext().getLoginUser();
//            String member = user.getId().toString();
            UserLocation userLocation = this.listUserLocation(user.getId());
            if (userLocation != null) {
                String username = userLocation.getNickname();
                String address = userLocation.getAddress();
                String userFriend = userLocation.getUserFriend();
                int deleted = userLocation.getDeleted();
                UserLocationVo userVO = new UserLocationVo(username, address, user.getId(), latitude, longitude, userFriend, deleted);
                nearbyUsers.add(userVO);
            }
        });
        return nearbyUsers;
    }
}
