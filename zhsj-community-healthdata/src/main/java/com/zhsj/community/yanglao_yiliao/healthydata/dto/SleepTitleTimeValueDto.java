package com.zhsj.community.yanglao_yiliao.healthydata.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户睡眠title+time+value返回实体
 * @date 2021/11/17 19:50
 */
@Data
@Accessors(chain = true)
public class SleepTitleTimeValueDto {

    /**
     * 时间title
     */
    private String timeTitle;

    /**
     * 时间点对应的总睡眠时长
     */
    private Integer totalSleepTime;

    /**
     * 时间点
     */
    private String timeValue;

    /**
     * 时间点对应的星期
     */
    private String timeWeek;

    /**
     * 时间点对应的深睡时长
     */
    private Integer deepSleepTime;

    /**
     * 时间点对应的浅睡时长
     */
    private Integer lightSleepTime;

    /**
     * 时间点对应的梦醒时长
     */
    private Integer wakeUpTime;

    /**
     * 每天的睡眠评分
     */
    private String sleepScore;
}
