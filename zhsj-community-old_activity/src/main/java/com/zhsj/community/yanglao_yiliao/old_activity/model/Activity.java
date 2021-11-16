package com.zhsj.community.yanglao_yiliao.old_activity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 活动的实体类
 * @create: 2021-11-10 16:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_activity")
public class Activity implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 活动类型
     */
    private String activityType;
    /**
     * 活动说明
     */
    private String activityExplain;
     /**
     * 附近人的距离
     */
    private String distance;
    /**
     * 距离
     */
    private  String beatadistancefrom;
    /**
     *语音
     */
    private  String voice;
    /**
     * 图片路径
     */
    private String pathUrl;
    /**
     * 用户昵称
     */
    private  String aUser;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 维度
     */
    private String latitude;
    /**
     * 性别
     */
    private  String sex;
    /**
     * 年龄
     */
    private  String age;
    /**
     * 用户id
     */
    private  String uId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除  0 否 | 1 是
     */
    @TableLogic
    private Long deleted;


}
