package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: Hu
 * @create: 2021-11-13 14:50
 **/
@Data
@TableName("t_event_week")
public class EventWeekEntity extends BaseEntity {
    /**
     * 事件id
     */
    private Long eventId;
    /**
     *  一周内的某天
     */
    private Integer week;

    /**
     *  用户uid
     */
    private String uid;


}
