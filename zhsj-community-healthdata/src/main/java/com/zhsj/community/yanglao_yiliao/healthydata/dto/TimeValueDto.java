package com.zhsj.community.yanglao_yiliao.healthydata.dto;

import lombok.Data;

/**
 * @author zzm
 * @version 1.0
 * @Description: 时间数据实体
 * @date 2021/11/15 15:58
 */
@Data
public class TimeValueDto {

    /**
     * 时间
     */
    private String time;
    /**
     * 数据
     */
    private Integer data;

    public TimeValueDto() {
    }

    public TimeValueDto(String time, Integer data) {
        this.time = time;
        this.data = data;
    }
}
