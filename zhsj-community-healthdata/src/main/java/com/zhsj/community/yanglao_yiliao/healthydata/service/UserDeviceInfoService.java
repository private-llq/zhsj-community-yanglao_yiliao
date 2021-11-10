package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserUnbindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.UserDeviceInfo;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户设备实体对应service层
 * @date 2021/11/10 13:49
 */
public interface UserDeviceInfoService extends IService<UserDeviceInfo> {

    /***************************************************************************************************************************
     * @description 用户绑定设备
     * @author zzm
     * @date 2021/11/10 14:18
     * @param userBindDeviceReqBo 设备信息
     **************************************************************************************************************************/
    void userBindDevice(UserBindDeviceReqBo userBindDeviceReqBo);

    /***************************************************************************************************************************
     * @description 用户解绑设备
     * @author zzm
     * @date 2021/11/10 15:37
     * @param userUnbindDeviceReqBo 解绑信息
     **************************************************************************************************************************/
    void userUnbindDevice(UserUnbindDeviceReqBo userUnbindDeviceReqBo);

}
