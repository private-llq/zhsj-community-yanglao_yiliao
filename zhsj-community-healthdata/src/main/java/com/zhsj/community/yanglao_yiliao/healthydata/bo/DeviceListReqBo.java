package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zzm
 * @version 1.0
 * @Description: 后台管理获取用户绑定设备列表请求参数
 * @date 2021/12/6 15:01
 */
@Data
public class DeviceListReqBo {

    /**
     * 设备名称（进行模糊搜索，可以为空）
     */
    private String deviceName;
    /**
     * 设备马克地址（进行模糊搜索，可以为空）
     */
    private String deviceAddress;
    /**
     * 当前分页页码
     */
    @NotNull(message = "分页页码不能为空")
    private Integer pageNo;
    /**
     * 当前分页每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;
}
