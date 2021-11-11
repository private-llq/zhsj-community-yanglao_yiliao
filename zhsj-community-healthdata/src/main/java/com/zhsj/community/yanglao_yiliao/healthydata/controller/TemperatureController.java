package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.alibaba.fastjson.JSON;
import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorTemperatureReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户体温对应控制层
 * @date 2021/11/11 13:59
 */
@RestController
@RequestMapping("/temperature")
@Validated
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    /***************************************************************************************************************************
     * @description 检测用户体温并保存
     * @author zzm
     * @date 2021/11/11 14:08
     * @param list 用户体温信息
     **************************************************************************************************************************/
    @PostMapping("/monitorTemperature")
    public R<Void> monitorTemperature(@RequestBody @Valid List<MonitorTemperatureReqBo> list) {
        temperatureService.monitorTemperature(list);
        return R.ok();
    }

    /***************************************************************************************************************************
     * @description 批量删除用户体温数据
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 体温id列表
     **************************************************************************************************************************/
    @PostMapping("/batchDeleteTemperature")
    public R<Void> batchDeleteTemperature(@RequestBody List<Long> list) {
        temperatureService.batchDeleteTemperature(list);
        return R.ok();
    }
}
