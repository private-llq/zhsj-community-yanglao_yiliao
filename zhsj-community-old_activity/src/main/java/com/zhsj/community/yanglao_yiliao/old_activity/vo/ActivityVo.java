package com.zhsj.community.yanglao_yiliao.old_activity.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 活动的扩展类
 * @create: 2021-11-10 17:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVo implements Serializable {
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
     * 语音
     */
    private String voice;
    /**
     * 图片路径
     */
    private String pathUrl;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 维度
     */
    private Double latitude;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private String age;
    /**
     * 用户id
     */
    private Long uId;


    /**
     * 逻辑删除  0 否 | 1 是
     */
    @TableLogic
    private int deleted;
}
