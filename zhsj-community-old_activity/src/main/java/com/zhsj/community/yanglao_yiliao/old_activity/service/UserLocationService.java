package com.zhsj.community.yanglao_yiliao.old_activity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.UserLocationFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description: 距离的业务接口
 * @create: 2021-11-11 15:10
 */
public interface UserLocationService {

                  /**============================================================================== */
    /**
     * 保存地理信息
     */
    Boolean saveUserLocation(String member, Double latitude, Double longitude);


    /**
     * 查询缓存项
     * @param member
     * @param distance
     * @param metric
     * @param limit
     * @return
     */
    GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(String member, Double distance,
                                                                           Metric metric, int limit);


    /**
     *列出给定坐标附近一定范围内的所有用户
     * @param latitude
     * @param longitude
     * @param distance
     * @param metric
     * @param limit
     * @return
     */
    GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(Double latitude, Double longitude,
                                                                           Double distance, Metric metric, int limit);


    /**
     * 获取两个用户之间的距离
     *
     *
     */
    Double getDistanceBetween(Integer userId, Integer otherUserId);


    /**
     * 获取用户的坐标
     *
     */
     Map<String, Double> getUserLocation(Integer userId);


    /**
     * 删除用户坐标
     * @param userId
     * @return
     */
      Boolean deleteUserLocation(Integer userId);



    /**
     * 列出给定坐标附近的用户
     * @param latitude 纬度
     * @param longitude 经度
     * @param limit 数量上限
     * @return 附近用户集合
     */
    List<UserLocationVo> listNearbyUsers(Double latitude, Double longitude, Integer limit);



    /**
     * 列出附近用户
     * @param userId 用户id
     * @param limit 上限
     * @return 附近用户集合
     */
    List<UserLocationVo> listNearbyUsersed(Long userId, Integer limit);

}
