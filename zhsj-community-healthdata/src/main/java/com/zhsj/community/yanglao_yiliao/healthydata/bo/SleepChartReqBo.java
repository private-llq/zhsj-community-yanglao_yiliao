package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 获取用户睡眠图表信息入参
 * @date 2021/11/17 15:20
 */
@Data
public class SleepChartReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 查询健康图表数据时间类型（以天:1、周:2）
     */
    @NotNull(message = "查询时间类型不能为空")
    @Range(min = 1, max = 2)
    private Integer timeStatus;

    /**
     * 按周查询睡眠上下周翻页状态（pageTurnStatus默认：-1，按天查询不传此参数）
     */
    @Range(min = -10000, max = -1)
    private Integer pageTurnStatus = -1;

}
