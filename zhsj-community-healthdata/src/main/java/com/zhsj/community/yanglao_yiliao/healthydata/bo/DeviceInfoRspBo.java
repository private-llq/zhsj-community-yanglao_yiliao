package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author zzm
 * @version 1.0
 * @Description: 获取用户设备信息返回值实体
 * @date 2021/11/13 13:54
 */
@Data
public class DeviceInfoRspBo {

    /**
     * 当前登录用户id
     */
    private String userUuid;

    /**
     * 家人id
     */
    private String familyMemberId;

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
}
