package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.zhsj.community.yanglao_yiliao.healthydata.bo.HeartRateChartReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataRspBo;

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

    void heartRateChart(HeartRateChartReqBo reqBo);
}
