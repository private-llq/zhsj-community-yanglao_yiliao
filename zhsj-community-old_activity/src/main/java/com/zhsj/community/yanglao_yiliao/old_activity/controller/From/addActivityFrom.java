package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-16 09:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class addActivityFrom {
    /**
     * 主键id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 活动类型
     */
    @NotNull(message = "活动类型不能为空")
    private String activityType;
    /**
     * 活动说明
     */
    private String activityExplain;
    /**
     *语音
     */
    private  String voice;
    /**
     * 图片路径
     */
    private List<String> pathUrl;
    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private String longitude;
    /**
     * 维度
     */
    @NotNull(message = "纬度不能为空")
    private String latitude;
    /**
     * 距离
     */
    @NotNull(message = "距离不能为空")
    private  String beatadistancefrom;




}
