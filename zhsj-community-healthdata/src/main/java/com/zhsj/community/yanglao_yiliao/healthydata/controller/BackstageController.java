package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.alibaba.fastjson.JSON;
import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.common.utils.PageVo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceListReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceListRspBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.UserDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 大后台管理控制层
 * @date 2021/12/6 11:42
 */
@RestController
@RequestMapping("/backstage/deviceManage")
public class BackstageController {

    @Autowired
    private UserDeviceInfoService deviceInfoService;

    /***************************************************************************************************************************
     * @description 大后台医疗养老设备管理-获取用户绑定设备列表
     * @author zzm
     * @date 2021/12/6 14:50
     * @param reqBo 查询参数
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.DeviceListRspBo>
     **************************************************************************************************************************/
    @PostMapping("/deviceList")
    public R<PageVo<DeviceListRspBo>> deviceList(@RequestBody @Valid DeviceListReqBo reqBo) {
        PageVo<DeviceListRspBo> rspBoPageVo = deviceInfoService.deviceList(reqBo);
        return R.ok(rspBoPageVo);
    }

    /***************************************************************************************************************************
     * @description 大后台医疗养老设备管理-删除用户设备绑定信息
     * @author zzm
     * @date 2021/12/6 17:21
     * @param ids 绑定设备的id集合
     **************************************************************************************************************************/
    @PostMapping("/deleteDevice")
    public R<Void> deleteDevice(@RequestBody List<Long> ids) {
        deviceInfoService.deleteDevice(ids);
        return R.ok();
    }
}
