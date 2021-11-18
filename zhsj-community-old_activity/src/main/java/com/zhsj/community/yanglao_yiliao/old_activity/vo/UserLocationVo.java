package com.zhsj.community.yanglao_yiliao.old_activity.vo;



import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private String id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 维度
     */
    private double latitude;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 距离
     */
    private  String beatadistancefrom;
    /**
     * 位置描述
     */
    private String address;



    public static UserLocationVo format(UserLocation userLocation) {

        UserLocationVo userLocationVo = new UserLocationVo();
        userLocationVo.setUserId(userLocation.getUserId());
        userLocationVo.setLongitude(userLocation.getLongitude());
        userLocationVo.setLatitude(userLocation.getLatitude());
        userLocationVo.setNickname(userLocation.getNickname());
        userLocationVo.setAddress(userLocation.getAddress());
        userLocationVo.setBeatadistancefrom(userLocation.getBeatadistancefrom());
        return userLocationVo;
    }

    public static List<UserLocationVo> formatToList(List<UserLocation> userLocations) {
        List<UserLocationVo> list = new ArrayList<>();
        for (UserLocation userLocation : userLocations) {
            list.add(format(userLocation));
        }
        return list;
    }
}
