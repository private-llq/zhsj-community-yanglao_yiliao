package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import com.zhsj.community.yanglao_yiliao.healthydata.dto.TempTitleTimeValueDto;
import lombok.Data;

import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户体温图表返回实体
 * @date 2021/11/17 9:47
 */
@Data
public class TempChartRspBo {

    /**
     * 体温平均值健康状态（1：正常，2：偏低，3：偏高）
     */
    private Integer tempStatus;

    /**
     * 体温平均值
     */
    private String temptAvg;

    /**
     * list<日体温 = title + time + value>
     */
    private List<TempTitleTimeValueDto> list;
}
