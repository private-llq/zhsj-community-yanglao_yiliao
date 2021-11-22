package com.zhsj.community.yanglao_yiliao.old_activity.service.impl;

import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.UserLocationFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.mapper.UserLocationMapper;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
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


    @Resource
    private UserLocationMapper userLocationMapper;

    @Resource
    private  UserLocationService userLocationService;

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
     *  ***************************************获取当前登录用户**********************************************************
     *       *****************************************************************************************************
     */
    private LoginUser userAuth() {
        return ContextHolder.getContext().getLoginUser();
    }


    /**
     * 验证坐标是否合法，经度: -180~180、纬度：-90~90
     * @param latitude 纬度
     * @param longitude 经度
     * @return 是否合法
     */
    private Boolean invalidCoordinates(Double latitude, Double longitude){
        return latitude == null ||
                longitude == null ||
                Math.abs(latitude) > 90 ||
                Math.abs(longitude) > 180;
    }


    @Override
    public Boolean saveUserLocation(String member, Double latitude, Double longitude) {
        if(member == null  || member.isEmpty() || invalidCoordinates(latitude, longitude)){
            return false;
        }
        // 封装坐标
        Point point = new Point(longitude, latitude);
        Long remove = redisTemplate.opsForZSet().remove(CACHE_KEY, member);
        log.info("清除用户：id=" + member + "过期位置信息, 状态：" + remove);
        Long status = 0L;
        //Add操作
        if(remove != null){
            status = redisTemplate.opsForGeo().add(CACHE_KEY, point, member);
        }
        return status != null && status > 0;
    }


    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(String member, Double distance, Metric metric, int limit) {
        if(member == null || member.isEmpty() || distance == null || distance <= 0 || metric == null || limit <= 0){
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
                .radius(CACHE_KEY,member, new Distance(distance, metric), commandArgs);
    }


    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(Double latitude, Double longitude, Double distance, Metric metric, int limit) {
        if(invalidCoordinates(latitude, longitude) || distance == null || distance <= 0 || metric == null || limit <= 0){
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
    public Double getDistanceBetween(Integer userId, Integer otherUserId){
        if(userId == null || otherUserId == null || userId <= 0 || otherUserId <= 0){
            throw new IllegalArgumentException();
        }
          //获取用户信息
//LoginUser user = ContextHolder.getContext().getLoginUser();
        Distance distance = redisTemplate
                .opsForGeo()
                .distance(CACHE_KEY, userId.toString(), otherUserId.toString(), Metrics.NEUTRAL);

        return distance==null ? null : distance.getValue();
    }

    @Override
    public Map<String, Double> getUserLocation(Integer userId) {
   //获取用户信息
        LoginUser user = ContextHolder.getContext().getLoginUser();
        List<Point> points = redisTemplate.opsForGeo().position(CACHE_KEY, user.getId().toString());
        if(points == null){
            return null;
        }
        Map<String, Double> result = new HashMap<>(8);
        result.put("latitude", points.get(0).getY());
        result.put("longitude", points.get(0).getX());
        return result;
    }

    @Override
    public Boolean deleteUserLocation(Integer userId) {
        if(userId == null || userId < 0){
            return false;
        }
        Long status = redisTemplate.opsForZSet().remove(CACHE_KEY, userId.toString());
        return status != null && status > 0;
    }

    @Override
    public List<UserLocationVo> listNearbyUsers(Double latitude, Double longitude, Integer limit){
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.userLocationService.listLocationsInRadius(latitude,
                longitude, DEFAULT_RADIUS, RADIUS_METRIC, limit > MAX_LIMIT ? MAX_LIMIT : limit);
        return geoResultToVO(results);
    }

    @Override
    public List<UserLocationVo> listNearbyUsersed(Long userId, Integer limit) {
        if(userId == null || limit == null || limit <= 0){
            throw new IllegalArgumentException();
        }
        String member = userId.toString();
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.userLocationService.listLocationsInRadius(member,
                DEFAULT_RADIUS, RADIUS_METRIC, limit > MAX_LIMIT ? MAX_LIMIT : limit);
        return geoResultToVO(results);
    }


    /**
     * 将redis geo的结果转换成页面显示结果
     * @param results redis geo结果集
     * @return 附近用户VO集合
     */
    private List<UserLocationVo> geoResultToVO(GeoResults<RedisGeoCommands.GeoLocation<String>> results){
        if(results == null){
            return null;
        }
        List<UserLocationVo> nearbyUsers = new ArrayList<>();
        results.forEach((result)-> {
            double distance = result.getDistance().getValue();
            Integer uId = Integer.valueOf(result.getContent().getName());
            double latitude = result.getContent().getPoint().getY();
            double longitude = result.getContent().getPoint().getX();

//            UserLocationFrom userDTO = this.userLocationMapper.getMatchedUserInfo(uId);
//            if (userDTO != null) {
//                String username = userDTO.getUser_name();
//                int gender = userDTO.getSex();
//                String birthday = userDTO.getBirthday();
//                String description = userDTO.getIntroduction();
//                String portraitUrl = userDTO.getHead_image_url();
//                NearbyUserVO userVO = new NearbyUserVO(uId, username, gender, birthday, description, portraitUrl, distance, latitude, longitude);
//                nearbyUsers.add(userVO);



//            }
        });
        return nearbyUsers;
    }
}
