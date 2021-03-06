package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 获取用户体温图表信息请求参数
 * @date 2021/11/17 9:56
 */
@Data
public class TempChartReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 查询健康图表数据时间类型（以天:1、周:2、月:3查）
     */
    @NotNull(message = "查询时间类型不能为空")
    @Range(min = 1, max = 3)
    private Integer timeStatus;
}
