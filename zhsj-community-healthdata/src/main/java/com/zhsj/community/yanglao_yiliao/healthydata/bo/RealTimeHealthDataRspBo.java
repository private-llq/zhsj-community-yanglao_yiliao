package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

/**
 * @author zzm
 * @version 1.0
 * @Description: 获取用户实时健康数据返回值对应实体
 * @date 2021/11/12 14:09
 */
@Data
public class RealTimeHealthDataRspBo {

    /**
     * 用户当前心率
     */
    private Integer silentHeart;
    /**
     * 用户当前心率健康状态
     */
    private Integer silentHeartHealthStatus;


    /**
     * 睡眠时间（返回分钟数）
     */
    private Integer sleepTime;
    /**
     * 睡眠健康状态
     */
    private Integer sleepHealthStatus;


    /**
     * 手腕温度
     */
    private Double tmpHandler;
    /**
     * 手腕温度健康状态
     */
    private Integer tmpHandlerHealthStatus;


    /**
     * 额头温度
     */
    private Double tmpForehead;
    /**
     * 额头温度健康状态
     */
    private Integer tmpForeheadHealthStatus;

    /**
     * 跟新数据时间
     */
    private String refreshDataTime;
}
