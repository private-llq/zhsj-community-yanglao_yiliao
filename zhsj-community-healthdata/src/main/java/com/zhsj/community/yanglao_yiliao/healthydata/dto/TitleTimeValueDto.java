package com.zhsj.community.yanglao_yiliao.healthydata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户心率体温睡眠title、时间点、值返回实体
 * @date 2021/11/16 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleTimeValueDto {

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
    private Integer healthData;
}
