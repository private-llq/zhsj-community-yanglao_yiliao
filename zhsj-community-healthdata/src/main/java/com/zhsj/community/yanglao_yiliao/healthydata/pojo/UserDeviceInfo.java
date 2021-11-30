package com.zhsj.community.yanglao_yiliao.healthydata.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户绑定硬件设备信息实体
 * @date 2021/11/10 12:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_user_device_info")
public class UserDeviceInfo {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 当前登录用户id
     */
    private String userUuid;

    /**
     * 设备名称
     */
    private String mDeviceName;

    /**
     * 设备地址(可以唯一确定设备)
     */
    private String mDeviceAddress;

    /**
     * 设备版本号
     */
    private String mDeviceVersion;

    /**
     * 设备电量
     */
    private Integer power;

    /**
     * 设备信号值
     */
    private Integer mRssi;

    /**
     * 是否绑定设备（0：未绑定false，1：已绑定true）
     */
    @TableLogic
    private Boolean bind;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


    public static UserDeviceInfo build(LoginUser user, UserBindDeviceReqBo bo) {
        return UserDeviceInfo.builder()
                .userUuid(user.getAccount())
                .mDeviceName(bo.getDeviceName())
                .mDeviceAddress(bo.getDeviceAddress())
                .mDeviceVersion(bo.getDeviceVersion())
                .power(bo.getPower())
                .mRssi(bo.getMRssi())
                .bind(true)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
