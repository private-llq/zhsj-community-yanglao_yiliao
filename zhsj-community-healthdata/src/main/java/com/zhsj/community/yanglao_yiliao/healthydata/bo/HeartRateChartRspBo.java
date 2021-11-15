package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzm
 * @version 1.0
 * @Description: 查询用户心率图表数据返回实体
 * @date 2021/11/15 19:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartRateChartRspBo {

    /**
     * 时间title
     */
    private String timeTitle;
    /**
     * 时间点
     */
    private String timeValue;
    /**
     * 时间点对应的心率平均值
     */
    private Integer heartRateCounts;

}
