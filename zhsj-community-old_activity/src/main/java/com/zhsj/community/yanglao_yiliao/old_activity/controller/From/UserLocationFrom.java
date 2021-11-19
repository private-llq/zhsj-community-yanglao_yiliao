package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author liulq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationFrom implements Serializable {

    private Long id;
    /**
     *用户id
     */
    private Long userId;
    /**
     * x:经度
     */
    @NotNull(message = "经度不能为空")
     private Double longitude;
    /**
     *维度
     */
    @NotNull(message = "维度不能为空")
    private Double latitude;
    /**
     *位置描述
     */
    @NotNull(message = "位置描述不能为空")
    private String address;
    /**
     *距离
     */
    @NotNull(message = "距离不能为空")
    private  String beatadistancefrom;

    /**
     * 限制
     */
    private  Integer limit;

}
