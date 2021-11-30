package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos我要找路
 * @author: Hu
 * @create: 2021-11-30 15:03
 **/
@Data
@TableName("t_route_sos")
public class RouteSosEntity extends BaseEntity {
    /**
     * 用户id
     */
    private String uid;
    /**
     * 经度
     */
    private BigDecimal lon;
    /**
     * 纬度
     */
    private BigDecimal lat;

}
