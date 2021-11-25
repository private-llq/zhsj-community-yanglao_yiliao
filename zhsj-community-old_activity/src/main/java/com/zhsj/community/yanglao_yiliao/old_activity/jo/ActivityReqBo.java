package com.zhsj.community.yanglao_yiliao.old_activity.jo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chengl
 * @version 1.0
 * @Description: 附近活动列表入参
 * @date 2021/11/23 19:41
 */
@Data
public class ActivityReqBo {

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private Double longitude;

    /**
     * 维度
     */
    @NotNull(message = "维度不能为空")
    private Double latitude;


    /**
     * 限制距离大小
     */
    private Double dist;
}
