package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2021-12-06 10:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityTypeDto {
    /**
     * 活动类型编码
     */
    @NotNull(message = "活动类型编码不能为空")
    private String activityTypeCode;


    /**
     * 活动类型名称
     */
    @NotNull(message = "活动类型名称不能为空")
    private String activityTypeName;
}
