package com.zhsj.community.yanglao_yiliao.healthydata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzm
 * @version 1.0
 * @Description:
 * @date 2021/11/22 15:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempTitleTimeValueDto {

    /**
     * 时间title
     */
    private String timeTitle;
    /**
     * 时间点
     */
    private String timeValue;
    /**
     * 时间点对应的体温平均值
     */
    private String healthData;
}
