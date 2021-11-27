package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author zzm
 * @version 1.0
 * @Description: 获取用户设备信息请求参数
 * @date 2021/11/13 13:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfoReqBo {

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;
}
