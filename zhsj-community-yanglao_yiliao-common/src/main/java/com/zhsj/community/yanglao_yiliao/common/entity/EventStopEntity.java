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
@TableName("t_event_stop")
public class EventStopEntity extends BaseEntity {
    /**
     * 事件id
     */
    private Long eventId;
    /**
     *  1当天停
     */
    private Integer stop;

    /**
     *  用户uid
     */
    private String uid;


}
