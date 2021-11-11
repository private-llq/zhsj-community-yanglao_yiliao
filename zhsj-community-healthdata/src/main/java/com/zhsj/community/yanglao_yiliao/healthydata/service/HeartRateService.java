package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorHeartRateReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.HeartRate;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户心率service层
 * @date 2021/11/11 9:23
 */
public interface HeartRateService extends IService<HeartRate> {

    /***************************************************************************************************************************
     * @description 监测用户实时心率以及历史心率并保存（历史心率需筛选）
     * @author zzm
     * @date 2021/11/11 9:34
     * @param list 心率信息
     **************************************************************************************************************************/
    void monitorHeartRate(List<MonitorHeartRateReqBo> list);

}
