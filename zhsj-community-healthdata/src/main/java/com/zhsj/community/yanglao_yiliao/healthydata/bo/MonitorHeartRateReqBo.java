package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 监测并保存心率请求参数
 * @date 2021/11/11 9:32
 */
@Data
public class MonitorHeartRateReqBo {

    /**
     * 用户心率
     */
    @NotNull(message = "用户心率不能为空")
    private Integer silentHeart;
    /**
     * 用户舒张压
     */
    @NotNull(message = "用户舒张压不能为空")
    private Integer diastolicPressure;
    /**
     * 用户收缩压
     */
    @NotNull(message = "用户收缩压不能为空")
    private Integer systolicPressure;
    /**
     * 心率时间
     */
    @NotNull(message = "心率时间不能为空")
    private Long createTime;
}


