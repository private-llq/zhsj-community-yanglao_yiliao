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

    /***************************************************************************************************************************
     * @description 获取用户实时健康数据
     * @author zzm
     * @date 2021/11/12 14:13
     * @param reqBo 用户信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataRspBo
     **************************************************************************************************************************/
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
    public R<List<AbnormalDataRspBo>> abnormalHeartRateRecord(@RequestBody @Valid AbnormalDataReqBo reqBo) {
        List<AbnormalDataRspBo> rspBoList = healthDataService.abnormalHeartRateRecord(reqBo);
        return R.ok(rspBoList);
    }

    /***************************************************************************************************************************
     * @description 查询用户体温图表信息
     * @author zzm
     * @date 2021/11/17 9:57
     * @param reqBo 用户信息、时间信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.TempChartRspBo
     **************************************************************************************************************************/
    @PostMapping("/tempChart")
    public R<TempChartRspBo> tempChart(@RequestBody @Valid TempChartReqBo reqBo) {
        TempChartRspBo tempChartRspBo = healthDataService.tempChart(reqBo);
        return R.ok(tempChartRspBo);
    }

    /***************************************************************************************************************************
     * @description 根据时间类型获取用户体温异常记录
     * @author zzm
     * @date 2021/11/16 17:04
     * @param reqBo 用户id，查询时间类型
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.AbnormalDataRspBo>
     **************************************************************************************************************************/
    @PostMapping("/abnormalTempRecord")
    public R<List<AbnormalDataRspBo>> abnormalTempRecord(@RequestBody @Valid AbnormalDataReqBo reqBo) {
        List<AbnormalDataRspBo> rspBoList = healthDataService.abnormalTempRecord(reqBo);
        return R.ok(rspBoList);
    }
}
