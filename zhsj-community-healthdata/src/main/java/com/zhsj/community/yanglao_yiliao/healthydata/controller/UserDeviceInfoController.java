package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserUnbindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.UserDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户设备控制层
 * @date 2021/11/10 15:10
 */
@RestController
@RequestMapping("/userDeviceInfo")
public class UserDeviceInfoController {

    @Autowired
    private UserDeviceInfoService userDeviceInfoService;

    /***************************************************************************************************************************
     * @description 用户绑定设备
     * @author zzm
     * @date 2021/11/10 14:18
     * @param userBindDeviceReqBo 设备信息
     **************************************************************************************************************************/
    @PostMapping("/userBindDevice")
    public R<Void> userBindDevice(@RequestBody @Valid UserBindDeviceReqBo userBindDeviceReqBo) {
        userDeviceInfoService.userBindDevice(userBindDeviceReqBo);
        return R.ok();
    }

    /***************************************************************************************************************************
     * @description 用户解绑设备
     * @author zzm
     * @date 2021/11/10 15:37
     * @param userUnbindDeviceReqBo 解绑信息
     **************************************************************************************************************************/
    @PostMapping("/userUnbindDevice")
    public R<Void> userUnbindDevice(@RequestBody @Valid UserUnbindDeviceReqBo userUnbindDeviceReqBo) {
        userDeviceInfoService.userUnbindDevice(userUnbindDeviceReqBo);
        return R.ok();
    }

    /***************************************************************************************************************************
     * @description 获取用户最后一次绑定设备信息
     * @author zzm
     * @date 2021/11/13 13:57
     * @param reqBo 用户信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo
     **************************************************************************************************************************/
    @PostMapping("/deviceInfo")
    public R<DeviceInfoRspBo> deviceInfo(@RequestBody @Valid DeviceInfoReqBo reqBo) {
        DeviceInfoRspBo deviceInfoRspBo = userDeviceInfoService.deviceInfo(reqBo);
        return R.ok(deviceInfoRspBo);
    }

    /***************************************************************************************************************************
     * @description 获取当前登录用户绑定的设备信息列表
     * @author zzm
     * @date 2021/11/27 17:53
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceInfoRspBo>
     **************************************************************************************************************************/
    @PostMapping("/currentLoginUserDeviceInfoList")
    public R<List<DeviceInfoRspBo>> currentLoginUserDeviceInfo() {
        List<DeviceInfoRspBo> list = userDeviceInfoService.currentLoginUserDeviceInfo();
        return R.ok(list);
    }
}
