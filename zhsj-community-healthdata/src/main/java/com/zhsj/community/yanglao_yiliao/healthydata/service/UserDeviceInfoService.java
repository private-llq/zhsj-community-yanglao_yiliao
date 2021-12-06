package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.common.utils.PageVo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.UserDeviceInfo;

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
     * @description 用户绑定切换设备
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

    // --------------------------------------------------------后台管理接口-----------------------------------------------------

    /***************************************************************************************************************************
     * @description 大后台医疗养老设备管理-获取用户绑定设备列表
     * @author zzm
     * @date 2021/12/6 14:50
     * @param reqBo 查询参数
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceListRspBo>
     **************************************************************************************************************************/
    PageVo<DeviceListRspBo> deviceList(DeviceListReqBo reqBo);

    /***************************************************************************************************************************
     * @description 大后台医疗养老设备管理-删除用户设备绑定信息
     * @author zzm
     * @date 2021/12/6 17:21
     * @param ids 绑定设备的id集合
     **************************************************************************************************************************/
    void deleteDevice(List<Long> ids);
}
