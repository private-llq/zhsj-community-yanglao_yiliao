package com.zhsj.community.yanglao_yiliao.old_activity.dto;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author chengl
 * @version 1.0
 * @Description: 附近活动返回实体
 * @date 2021/11/24 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    /**
     * id
     */
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
    private String voiceUrl;
    /**
     * 用户的聊天id
     */
    private String imId;
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
     * 1:自己 ，0 不是自己
     */
    private Boolean isUser;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 距离
     */
    private Long distance;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 出生年月
     */
    private String birthday;
    /**
     * 头像图片
     */
    private String avatarImages;
    /**
     * 维度
     */
    private Double latitude;
    /**
     * 语音的时长
     */
    private Integer voiceTime;
    /**
     * 是否是自己   1 是 | 0 不是
     */
    private Integer userSelf;
    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;
    /**
     * 语音文件的大小
     */
    private int voiceFileSize;
    /**
     * 转换的时间秒数
     */
    private Long publishTimed;

    /**
     * 是否删除（0：已删除false，1：未删除true）
     */
    @TableLogic
    private Boolean deleted;


}
