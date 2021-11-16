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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationVo implements Serializable {

    private String id;
    //用户id
    private Long userId;
    //经度
    private Double longitude;
    //维度
    private Double latitude;
    //位置描述
    private String address;
    //昵称
    private String nickname;



    public static UserLocationVo format(UserLocation userLocation) {

        UserLocationVo userLocationVo = new UserLocationVo();
        userLocationVo.setAddress(userLocation.getAddress());
        userLocationVo.setUserId(userLocation.getUserId());
        userLocationVo.setLongitude(userLocation.getLongitude());
        userLocationVo.setLatitude(userLocation.getLatitude());
        userLocationVo.setNickname(userLocation.getNickname());
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
