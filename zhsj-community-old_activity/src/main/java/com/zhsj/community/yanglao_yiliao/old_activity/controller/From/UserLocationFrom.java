package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;


/**
 * @author liulq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationFrom  {

    private Long id;
    /**
     *用户id
     */
    private Long userId;
     /**
     *活动id
     */
    private Long aId;
    /**
     * x:经度
     */
    @NotNull(message = "经度不能为空")
     private Double longitude;
    /**
     * y:维度
     */
    @NotNull(message = "维度不能为空")
    private Double latitude;
    /**
     *位置描述
     */
    @NotNull(message = "位置描述不能为空")
    private String address;
    /**
     * 是否是好友 0 否 | 1 是
     */
    @NotNull(message = "好友不能为空")
    private  String userFriend;

    /**
     * 限制
     */
    private  Integer limit;




}
