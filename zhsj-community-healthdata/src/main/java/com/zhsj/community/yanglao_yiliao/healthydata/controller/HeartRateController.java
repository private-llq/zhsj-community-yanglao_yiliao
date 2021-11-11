package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorHeartRateReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户心率控制层
 * @date 2021/11/11 9:25
 */
@Validated
@RestController
@RequestMapping("/heartRate")
public class HeartRateController {

    @Autowired
    private HeartRateService heartRateService;

    /***************************************************************************************************************************
     * @description 监测用户实时心率以及历史心率并保存（历史心率需筛选）
     * @author zzm
     * @date 2021/11/11 9:34
     * @param list 心率信息
     **************************************************************************************************************************/
    @PostMapping("/monitorHeartRate")
    public R<Void> monitorHeartRate(@RequestBody @Valid List<MonitorHeartRateReqBo> list) {
        heartRateService.monitorHeartRate(list);
        return R.ok();
    }

    /***************************************************************************************************************************
     * @description 批量删除用户心率数据
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 心率id列表
     **************************************************************************************************************************/
    @PostMapping("/batchDeleteHeartRate")
    public R<Void> batchDeleteHeartRate(@RequestBody List<Long> list) {
        heartRateService.batchDeleteHeartRate(list);
        return R.ok();
    }
}
