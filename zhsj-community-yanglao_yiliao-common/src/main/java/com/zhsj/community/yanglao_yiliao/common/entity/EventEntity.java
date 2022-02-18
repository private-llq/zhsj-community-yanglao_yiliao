package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
     * 内容
     */
    @NotBlank(message = "内容不能为空！",groups = {EventValidate.class})
    private String content;
    /**
     * 0未启动，1已启动
     */
    private Integer status;

    /**
     * 小时
     */
    @Range(min = 0,max = 23, message = "小时只能是0点到23点之间",groups = {EventValidate.class})
    @NotNull(message = "小时不能为空！",groups = {EventValidate.class})
    private Integer warnHour;

    /**
     * 分钟
     */
    @Range(min = 0,max = 59, message = "分钟只能是0分到59分之间",groups = {EventValidate.class})
    @NotNull(message = "分钟不能为空！",groups = {EventValidate.class})
    private Integer warnMinute;


    /**
     * 是否提醒，0未提醒，1已提醒
     */
    private Integer pushStatus;

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
     * 事件周天
     */
    @NotEmpty(message = "事件周天不能为空！",groups = {EventValidate.class})
    @TableField(exist = false)
    private List<Integer> weeks = new LinkedList<>();

    /**
     * 添加修改验证
     */
    public interface EventValidate{}
}
