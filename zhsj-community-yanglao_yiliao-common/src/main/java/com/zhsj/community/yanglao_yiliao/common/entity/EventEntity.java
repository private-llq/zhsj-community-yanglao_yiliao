package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒
 * @author: Hu
 * @create: 2021-11-12 14:34
 **/
@Data
@TableName("t_event")
public class EventEntity extends BaseEntity {
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 1单次提醒，2每天提醒，3每周提醒，4每月提醒
     */
    private Integer type;
    /**
     * 内容
     */
    private String content;
    /**
     * 0未启动，1已启动
     */
    private Integer status;

    /**
     * 月
     */
    private Integer warnMonth;

    /**
     * 周
     */
    private Integer warnWeek;

    /**
     * 日
     */
    private Integer warnDay;

    /**
     * 小时
     */
    private Integer warnHour;

    /**
     * 分钟
     */
    private Integer warnMinute;

    /**
     * 年
     */
    private Integer warnYear;

    /**
     * 设置对应的时间
     */
    private LocalDate warnTime;


    /**
     * 接收前端家人id
     */
    @TableField(exist = false)
    private List<EventFamilyEntity> families = new LinkedList<>();


    /**
     * 返回前端家人信息
     */
    @TableField(exist = false)
    private List<Map<String, Object>> records = new LinkedList<>();
}
