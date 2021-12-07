package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.community.yanglao_yiliao.old_activity.common.pageVoed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2021-12-07 10:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeActivityDto extends pageVoed {

    /**
     * 活动类型名称
     */
    private String activityTypeName;
    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;
    /**
     * 当前登录用户名称
     */
    private String userName;
    /**
     * 手机号
     */
    private  String phone;

}
