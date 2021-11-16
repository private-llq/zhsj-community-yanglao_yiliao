package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.vo.R;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HealthDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户健康数据控制层
 * @date 2021/11/12 14:49
 */
@RestController
@RequestMapping("/healthData")
public class HealthDataController {

    @Autowired
    private HealthDataService healthDataService;

    @PostMapping("/realTimeHealthData")
    public R<RealTimeHealthDataRspBo> realTimeHealthData(@RequestBody @Valid RealTimeHealthDataReqBo reqBo) {
        RealTimeHealthDataRspBo healthDataRspBo = healthDataService.realTimeHealthData(reqBo);
        return R.ok(healthDataRspBo);
    }

    /***************************************************************************************************************************
     * @description 查询用户心率图表信息
     * @author zzm
     * @date 2021/11/16 15:47
     * @param reqBo 用户信息和时间信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.HeartRateChartRspBo
     **************************************************************************************************************************/
    @PostMapping("/heartRateChart")
    public R<HeartRateChartRspBo> heartRateChart(@RequestBody @Valid HeartRateChartReqBo reqBo) {
        HeartRateChartRspBo rspBos = healthDataService.heartRateChart(reqBo);
        return R.ok(rspBos);
    }

    /***************************************************************************************************************************
     * @description 获取用户心率异常记录列表
     * @author zzm
     * @date 2021/11/15 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     **************************************************************************************************************************/
    @PostMapping("/abnormalHeartRateRecord")
    public R<List<AbnormalDataRspBo>> abnormalHeartRateRecord(@RequestBody @Valid AbnormalHeartRateRecordReqBo reqBo) {
        List<AbnormalDataRspBo> rspBoList = healthDataService.abnormalHeartRateRecord(reqBo);
        return R.ok(rspBoList);
    }
}