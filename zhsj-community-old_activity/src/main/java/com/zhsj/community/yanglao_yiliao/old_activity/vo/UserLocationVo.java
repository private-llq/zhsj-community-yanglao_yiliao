package com.zhsj.community.yanglao_yiliao.old_activity.vo;


import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.model.UserLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liulq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationVo implements Serializable {

    private static final long serialVersionUID = 4133419501260037769L;

    private String id;
    private Long userId; //用户id
    private Double longitude; //经度
    private Double latitude; //维度
    private String address; //位置描述
    private String nickname;//昵称
    private LocalDateTime created; //创建时间
    private LocalDateTime updated; //更新时间


    public static final UserLocationVo format(UserLocation userLocation) {
//        获取用户信息
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();

        UserLocationVo userLocationVo = new UserLocationVo();
        userLocationVo.setAddress(userLocation.getAddress());
        userLocationVo.setCreated(userLocation.getCreateTime());
        userLocationVo.setUpdated(userLocation.getUpdateTime());
        userLocationVo.setUserId(userLocation.getUserId());
        userLocationVo.setLongitude(userLocation.getLongitude());
        userLocationVo.setLatitude(userLocation.getLatitude());
        return userLocationVo;
    }

    public static final List<UserLocationVo> formatToList(List<UserLocation> userLocations) {
        List<UserLocationVo> list = new ArrayList<>();
        for (UserLocation userLocation : userLocations) {
            list.add(format(userLocation));
        }
        return list;
    }
}
