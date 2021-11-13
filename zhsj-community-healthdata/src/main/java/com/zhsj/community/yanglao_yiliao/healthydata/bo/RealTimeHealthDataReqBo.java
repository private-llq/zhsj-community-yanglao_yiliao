package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zzm
 * @version 1.0
 * @Description: 获取用户实时健康数据请求参数
 * @date 2021/11/12 14:06
 */
@Data
public class RealTimeHealthDataReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;
}
