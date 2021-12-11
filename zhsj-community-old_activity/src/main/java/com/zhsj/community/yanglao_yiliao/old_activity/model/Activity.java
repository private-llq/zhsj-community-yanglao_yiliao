package com.zhsj.community.yanglao_yiliao.old_activity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.old_activity.dto.ActivityReqBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author chengl
 * @version 1.0
 * @Description: 活动对应实体
 * @date 2021/11/23 19:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_activity")
public class Activity {

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
    private String voiceUrl;

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
     * 经度
     */
    private Double longitude;
    /**
     * 语音文件的大小
     */
    private int voiceFileSize;
    /**
     * 维度
     */
    private Double latitude;
    /**
     * 语音的时长
     */
    private int voiceTime;
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

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public static Activity build(LoginUser user, ActivityReqBo reqBo, LocalDateTime localDateTime) {
        return Activity.builder()
                .userUuid(user.getAccount())
                .userName(user.getNickName())
//                .systolicPressure(reqBo.getSystolicPressure())
                .publishTime(localDateTime)
                .build();
    }
}

