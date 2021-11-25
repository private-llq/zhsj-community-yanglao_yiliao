package com.zhsj.community.yanglao_yiliao.old_activity.jo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chengl
 * @version 1.0
 * @Description: 附近活动新增入参
 * @date 2021/11/23 19:41
 */
@Data
public class ActivitySaveReqBo {

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动类型编码
     */
    @NotNull(message = "活动类型不能为空")
    private String activityTypeCode;

    /**
     * 图片地址3张，逗号分割
     */
    private String picUrl;

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
}
