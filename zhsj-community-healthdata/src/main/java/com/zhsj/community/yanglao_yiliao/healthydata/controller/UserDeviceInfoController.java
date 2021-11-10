package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserBindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.UserUnbindDeviceReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.UserDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public R<Void> userBindDevice(@RequestBody @Validated UserBindDeviceReqBo userBindDeviceReqBo) {
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
    public R<Void> userUnbindDevice(@RequestBody @Validated UserUnbindDeviceReqBo userUnbindDeviceReqBo) {
        userDeviceInfoService.userUnbindDevice(userUnbindDeviceReqBo);
        return R.ok();
    }
}
