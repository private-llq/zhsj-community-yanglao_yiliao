package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户绑定设备请求参数实体
 * @date 2021/11/10 14:12
 */
@Data
public class UserBindDeviceReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    private String  deviceName;

    /**
     * 设备地址
     */
    @NotBlank(message = "设备地址不能为空")
    private String deviceAddress;

    /**
     * 设备版本号
     */
    @NotBlank(message = "设备版本号不能为空")
    private String deviceVersion;

    /**
     * 设备电量
     */
    private Integer power;

    /**
     * 设备信号值
     */
    private Integer mRssi;
}
