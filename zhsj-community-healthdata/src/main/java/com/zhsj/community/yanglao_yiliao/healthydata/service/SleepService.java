package com.zhsj.community.yanglao_yiliao.healthydata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorSleepReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Sleep;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户睡眠对应实体service层
 * @date 2021/11/11 15:18
 */
public interface SleepService extends IService<Sleep> {

    /***************************************************************************************************************************
     * @description 监控用户睡眠并保存
     * @author zzm
     * @date 2021/11/11 15:32
     * @param list 用户睡眠信息
     **************************************************************************************************************************/
    void monitorSleep(List<MonitorSleepReqBo> list);
}
