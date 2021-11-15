package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 健康数据页面实时健康数据service层
 * @date 2021/11/12 13:48
 */

public interface HealthDataService {

    /***************************************************************************************************************************
     * @description 获取用户实时健康数据
     * @author zzm
     * @date 2021/11/12 14:13
     * @param reqBo 用户信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataRspBo
     **************************************************************************************************************************/
    RealTimeHealthDataRspBo realTimeHealthData(RealTimeHealthDataReqBo reqBo);

    // 查询心率图表信息
    List<HeartRateChartRspBo> heartRateChart(HeartRateChartReqBo reqBo);

    /***************************************************************************************************************************
     * @description 获取用户心率异常记录列表
     * @author zzm
     * @date 2021/11/15 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     **************************************************************************************************************************/
    List<AbnormalDataRspBo> abnormalHeartRateRecord(AbnormalHeartRateRecordReqBo reqBo);
}
