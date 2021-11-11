package com.zhsj.community.yanglao_yiliao.old_activity.vo;


import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-10 17:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVo {
    /**
     * 主键id
     */
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
     * 图片路径
     */
    private String pathUrl;
    /**
     * 用户昵称
     */
    private  String aUser;
    /**
     * 附近人的距离
     */
    private String distance;
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
     * 逻辑删除  0 否 | 1 是
     */
    @TableLogic
    private Long deleted;
}
