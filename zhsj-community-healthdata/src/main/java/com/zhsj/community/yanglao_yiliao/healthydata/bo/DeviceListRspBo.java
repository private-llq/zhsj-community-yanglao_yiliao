package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

/**
 * @author zzm
 * @version 1.0
 * @Description: 后台设备管理返回设备列表实体
 * @date 2021/12/6 14:38
 */
@Data
public class DeviceListRspBo {

    /**
     * id
     */
    private Long id;
    /**
     * 绑定设备用户的id
     */
    private String userUuid;
    /**
     * 绑定设备用户的昵称
     */
    private String nickName;
    /**
     * 1、男， 2、女
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 设备名称
     */
    private String mDeviceName;
    /**
     * 设备地址(可以唯一确定设备)
     */
    private String mDeviceAddress;
    /**
     * 登记时间（就是设备绑定时间）
     */
    private String filingTime;
}
