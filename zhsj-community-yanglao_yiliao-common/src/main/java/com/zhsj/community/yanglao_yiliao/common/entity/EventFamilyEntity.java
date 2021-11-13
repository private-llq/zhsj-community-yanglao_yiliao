package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件家人关联中间表
 * @author: Hu
 * @create: 2021-11-12 14:40
 **/
@Data
@TableName("t_event_family")
public class EventFamilyEntity extends BaseEntity {
    /**
     * 事件id
     */
    private Long eventId;
    /**
     * 家人id
     */
    private Long familyId;
    /**
     * 用户id
     */
    private String uid;

}
