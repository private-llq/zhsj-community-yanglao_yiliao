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
     * 睡眠时间（返回分钟数）
     */
    private Integer sleepTime;

    /**
     * 手腕温度
     */
    private Double tmpHandler;

    /**
     * 额头温度
     */
    private Double tmpForehead;
}
