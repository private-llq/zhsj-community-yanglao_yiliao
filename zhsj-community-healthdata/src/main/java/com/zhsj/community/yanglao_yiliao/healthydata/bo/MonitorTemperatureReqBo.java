package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 检测用户体温并保存请求参数
 * @date 2021/11/11 14:02
 */
@Data
public class MonitorTemperatureReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 手腕温度
     */
    @NotNull(message = "用户手腕温度不能为空")
    private Double tmpHandler;

    /**
     * 额头温度
     */
    @NotNull(message = "用户额头温度不能为空")
    private Double tmpForehead;

    /**
     * 体温时间
     */
    @NotNull(message = "用户体温时间不能为空")
    private Long createTime;
}
