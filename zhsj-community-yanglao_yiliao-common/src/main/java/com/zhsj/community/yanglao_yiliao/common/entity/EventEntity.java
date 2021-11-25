package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
     * 苹果透传字段
     */
    private String notiId;

    /**
     * 1单次提醒，2每天提醒，3每周提醒，4每月提醒
     */
    @Range(min = 1,max = 4, message = "类型不存在",groups = {EventValidate.class})
    private Integer type;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空！",groups = {EventValidate.class})
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
    @Range(min = 0,max = 23, message = "小时只能是0点到23点之间",groups = {EventValidate.class})
    @NotNull(message = "小时不能为空！",groups = {EventValidate.class})
    private Integer warnHour;

    /**
     * 分钟
     */
    @Range(min = 0,max = 60, message = "分钟只能是0分到60分之间",groups = {EventValidate.class})
    @NotNull(message = "分钟不能为空！",groups = {EventValidate.class})
    private Integer warnMinute;

    /**
     * 年
     */
    private Integer warnYear;

    /**
     * 设置对应的时间
     */
    @NotNull(message = "提醒时间不能为空！",groups = {EventValidate.class})
    private LocalDate warnTime;


    /**
     * 接收前端家人id
     */
    @TableField(exist = false)
    @NotEmpty(message = "提醒家人不能空！",groups = {EventValidate.class})
    private List<Long> families = new LinkedList<>();


    /**
     * 返回前端家人信息
     */
    @TableField(exist = false)
    private List<Map<String, Object>> records = new LinkedList<>();

    /**
     * 添加修改验证
     */
    public interface EventValidate{}
}
