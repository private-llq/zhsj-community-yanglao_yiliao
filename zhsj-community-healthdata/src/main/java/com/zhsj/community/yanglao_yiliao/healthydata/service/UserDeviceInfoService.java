package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserUnbindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.UserDeviceInfo;
import org.checkerframework.checker.units.qual.Current;

import java.util.List;

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

    /***************************************************************************************************************************
     * @description 获取用户最后一次绑定设备信息
     * @author zzm
     * @date 2021/11/13 13:57
     * @param reqBo 用户信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo
     **************************************************************************************************************************/
    DeviceInfoRspBo deviceInfo(DeviceInfoReqBo reqBo);

    /***************************************************************************************************************************
     * @description 获取当前登录用户绑定的设备信息
     * @author zzm
     * @date 2021/11/27 17:53
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo>
     **************************************************************************************************************************/
    List<DeviceInfoRspBo> currentLoginUserDeviceInfo();
}
