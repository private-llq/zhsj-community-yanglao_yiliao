package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorTemperatureReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Temperature;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户温度对应service层
 * @date 2021/11/11 13:56
 */
public interface TemperatureService extends IService<Temperature> {

    /***************************************************************************************************************************
     * @description 检测用户体温并保存
     * @author zzm
     * @date 2021/11/11 14:08
     * @param list 用户体温信息
     **************************************************************************************************************************/
    void monitorTemperature(List<MonitorTemperatureReqBo> list);
}
