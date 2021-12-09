package com.zhsj.community.yanglao_yiliao.old_activity.dto;




import com.zhsj.community.yanglao_yiliao.old_activity.common.PageVoed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2021-11-27 12:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPageDto extends PageVoed {
    /**
     * id
     */
    @NotNull(message = "用户id不能为空")
    private String id;

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
