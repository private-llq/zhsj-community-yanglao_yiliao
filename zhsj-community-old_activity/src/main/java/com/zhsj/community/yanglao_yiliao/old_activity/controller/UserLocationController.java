package com.zhsj.community.yanglao_yiliao.old_activity.controller;


import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.controller.From.UserLocationFrom;
import com.zhsj.community.yanglao_yiliao.old_activity.service.UserLocationService;
import com.zhsj.community.yanglao_yiliao.old_activity.vo.UserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_yiliao
 * @description: 距离相关的接口
 * @create: 2021-11-11 15:13
 */
@RestController
@RequestMapping("userLocation")
@Slf4j
public class  UserLocationController {

    @Autowired
    private UserLocationService userLocationService;


    /**
     * 更新当前用户的位置
     *
     * @return 更新状态
     */
    @PostMapping("save")
    public Boolean saveUserLocation(@RequestBody UserLocationFrom userLocationFrom){
        log.info("userLocationFrom的值：{}",userLocationFrom);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        String member = user.getId().toString();
        return this.userLocationService.saveUserLocation(member, userLocationFrom.getLatitude(), userLocationFrom.getLongitude());
    }


    /**
     * 获取该用户附近的用户
     *
     * @return 附近用户集合
     */
    @GetMapping("/nearby/{userId}")
    public List<UserLocationVo> listNearbyUsered(@RequestBody UserLocationFrom userLocationFrom){
        log.info("userLocationFrom的值是：{}",userLocationFrom);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        return this.userLocationService.listNearbyUsersed(user.getId(),userLocationFrom.getLimit());
    }



    /**
     * 获取某坐标附近的所有用户
     *
     * @return 附近用户集合
     */
    @GetMapping("/nearby")
    public List<UserLocationVo> listNearbyUsers(@RequestBody UserLocationFrom userLocationFrom){
        log.info("userLocationFrom的值是1：{}",userLocationFrom);
        return this.userLocationService.listNearbyUsers(userLocationFrom.getLatitude(),userLocationFrom.getLongitude(),userLocationFrom.getLimit());
    }


    /**
     * 获取两个用户之间的距离
     * @param userId 用户1id
     * @param otherUserId 用户2 id
     * @return 距离 单位km
     */
    @GetMapping("/distance")
    public Double getDistanceBetween(@RequestParam(value = "userId") Integer userId,
                                     @RequestParam(value = "otherUserId") Integer otherUserId){
        log.info("userId:{}",userId,"otherUserId:{}",otherUserId);
        try{
            return this.userLocationService.getDistanceBetween(userId, otherUserId);
        }
        catch (IllegalArgumentException e){
            log.error("距离参数错误");
            return null;
        }
    }


    /**
     * 获取用户坐标
     * @param userId 用户id
     * @return 坐标点
     */
    @GetMapping("/{userId}")
    public Map<String, Double> getUserLocation(@PathVariable("userId") Integer userId){
        log.info("userid的值：{}",userId);
        try{
            return this.userLocationService.getUserLocation(userId);
        }
        catch (IllegalArgumentException e){
            log.error("获取坐标参数错误");
            return null;
        }
    }

    /**
     * 删除用户位置信息
     * @param userId 用户id
     * @return 删除状态
     */
    @DeleteMapping("/delete")
    public Boolean deleteUserLocation(@RequestParam("userId") Integer userId){
        log.info("userid的值：{}",userId);
        return this.userLocationService.deleteUserLocation(userId);
    }








}
