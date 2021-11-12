package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 检测用户睡眠并保存请求参数
 * @date 2021/11/11 15:28
 */
@Data
public class MonitorSleepReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 睡眠计数步数
     */
    @NotNull(message = "睡眠步数不能为空")
    private Integer stepCount;

    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    @NotNull(message = "睡眠状态不能为空")
    private Integer sleepStatus;

    /**
     * 睡眠时间
     */
    @NotNull(message = "睡眠时间不能为空")
    private Long createTime;
}
