package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 个人设置
 * @author: Hu
 * @create: 2021-11-25 16:11
 **/
@Data
@TableName("t_user_setting")
public class UserSettingEntity extends BaseEntity {
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 消息提醒，0关闭，1开启
     */
    private Integer notificationStatus;
    /**
     * 开启震动，0关闭，1开启
     */
    private Integer shakeStatus;

}
