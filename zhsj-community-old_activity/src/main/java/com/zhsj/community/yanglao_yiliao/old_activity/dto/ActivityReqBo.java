package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import com.zhsj.community.yanglao_yiliao.old_activity.common.pageVoed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author chengl
 * @version 1.0
 * @Description: 附近活动列表入参
 * @date 2021/11/23 19:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityReqBo  extends pageVoed{

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private String longitude;

    /**
     * 维度
     */
    @NotNull(message = "维度不能为空")
    private String latitude;

}
