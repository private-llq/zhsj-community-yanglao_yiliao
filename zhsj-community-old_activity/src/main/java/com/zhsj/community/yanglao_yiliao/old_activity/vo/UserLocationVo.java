package com.zhsj.community.yanglao_yiliao.old_activity.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 距离的扩展类
 * @create: 2021-11-11 17:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationVo implements Serializable {

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 活动id
     */
    private Long aId;

    /**
     * 经度
     */
    private Double longitude;
    /**
     * 维度
     */
    private Double latitude;
    /**
     * 位置描述
     */
    private String address;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 是否是好友 0 否 | 1 是
     */
    private String userFriend;

    /**
     * 逻辑删除  0 否 | 1 是
     */
    @TableLogic
    private int deleted;

    public UserLocationVo(String username, String address, Long uId, double latitude, double longitude, String userFriend, int deleted) {
        ActivityVo activityVo = new ActivityVo();
        activityVo.setNickname(username);
        activityVo.setUId(uId);
        activityVo.setLatitude(latitude);
        activityVo.setLongitude(longitude);
        UserLocation userLocation = new UserLocation();
        userLocation.setAddress(address);
        userLocation.setDeleted(deleted);
        userLocation.setUserFriend(userFriend);
    }

}
