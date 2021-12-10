package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import com.zhsj.community.yanglao_yiliao.old_activity.common.PageVoed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2021-12-07 10:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeActivityDto extends PageVoed {

    /**
     * 活动类型名称
     */
    private String activityTypeName;
    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 当前登录用户名称
     */
    private String userName;
    /**
     * 手机号
     */
    private  String phone;

}
