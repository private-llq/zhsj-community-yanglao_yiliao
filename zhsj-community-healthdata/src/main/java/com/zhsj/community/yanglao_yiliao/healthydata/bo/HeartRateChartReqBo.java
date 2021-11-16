package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 心率统计图表详情请求参数
 * @date 2021/11/13 10:54
 */
@Data
public class HeartRateChartReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 查询健康图表数据时间类型（以天:1、周:2、月:3查）
     */
    @NotNull(message = "查询时间类型不能为空")
    private Integer timeStatus;
}