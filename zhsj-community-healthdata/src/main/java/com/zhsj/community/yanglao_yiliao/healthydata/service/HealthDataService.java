package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Temperature;

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

    /***************************************************************************************************************************
     * @description 查询用户心率图表信息
     * @author zzm
     * @date 2021/11/16 15:47
     * @param reqBo 用户信息和时间信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.HeartRateChartRspBo
     **************************************************************************************************************************/
    HeartRateChartRspBo heartRateChart(HeartRateChartReqBo reqBo);

    /***************************************************************************************************************************
     * @description 获取用户心率异常记录列表
     * @author zzm
     * @date 2021/11/15 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     **************************************************************************************************************************/
    List<AbnormalDataRspBo> abnormalHeartRateRecord(AbnormalDataReqBo reqBo);

    /***************************************************************************************************************************
     * @description 查询用户体温图表信息
     * @author zzm
     * @date 2021/11/17 9:57
     * @param reqBo 用户信息、时间信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.TempChartRspBo
     **************************************************************************************************************************/
    TempChartRspBo tempChart(TempChartReqBo reqBo);

    /***************************************************************************************************************************
     * @description 根据时间类型获取用户体温异常记录
     * @author zzm
     * @date 2021/11/16 17:04
     * @param reqBo 用户id，查询时间类型
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.AbnormalDataRspBo>
     **************************************************************************************************************************/
    List<AbnormalDataRspBo> abnormalTempRecord(AbnormalDataReqBo reqBo);
}
