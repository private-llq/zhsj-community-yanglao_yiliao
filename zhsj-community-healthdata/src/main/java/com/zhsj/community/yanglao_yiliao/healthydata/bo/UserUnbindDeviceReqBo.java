package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户解绑设备参数实体
 * @date 2021/11/10 15:25
 */
@Data
public class UserUnbindDeviceReqBo {

    @NotBlank(message = "用户设备地址不能为空")
    private String deviceAddress;
}
