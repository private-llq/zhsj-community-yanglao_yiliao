package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import com.zhsj.community.yanglao_yiliao.healthydata.dto.TitleTimeValueDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
     * 心率平均值健康状态（1：正常，2：偏低，3：偏高）
     */
    private Integer heartRateStatus;

    /**
     * 心率平均值
     */
    private Integer silentHeartAvg;

    /**
     * 日心率title-time-value
     */
    private List<TitleTimeValueDto> list;
}
