package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2021-12-06 16:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReqDto {
    /**
     * id
     */
    private Long id;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动类型名称
     */
    private String activityTypeName;
    /**
     * 图片地址3张，逗号分割
     */
    private String picUrl;
    /**
     * 当前登录用户名称
     */
    private String userName;

    /**
     * 当前登录用户id
     */
    private String userUuid;
    /**
     * 语音的url
     */
    private String voiceUrl;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别  1 男生  | 0  女生
     */
    private Integer sex;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 是否删除（0：已删除false，1：未删除true）
     */
    @TableLogic
    private Boolean deleted;

}
