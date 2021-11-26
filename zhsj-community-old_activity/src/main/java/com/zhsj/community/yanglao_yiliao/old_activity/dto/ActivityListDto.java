package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @create: 2021-11-26 16:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityListDto {
    /**
     * id
     */
    @TableId
    private Long id;


    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动类型编码
     */
    private String activityTypeCode;

    /**
     * 活动类型名称
     */
    private String activityTypeName;
    /**
     * 图片地址3张，逗号分割
     */
    private String picUrl;
    /**
     * 语音的url
     */
    private  String voiceUrl;

    /**
     * 当前登录用户id
     */
    private String userUuid;
    /**
     * 当前登录用户名称
     */
    private String userName;
    /**
     * 是否好友（0：否false，1：是true）
     */
    private Boolean isFriend;


    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;


    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;


    /**
     * 是否删除（0：已删除false，1：未删除true）
     */
    @TableLogic
    private Boolean deleted;

}
