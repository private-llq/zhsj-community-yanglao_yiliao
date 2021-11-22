package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos机构信息
 * @author: Hu
 * @create: 2021-11-11 14:03
 **/
@Data
@TableName("t_agency_sos")
public class AgencySosEntity extends BaseEntity {
    /**
     * 机构id
     */
    @NotNull(message = "机构不能为空！")
    private Long agencyId;

    @TableField(exist = false)
    private String agencyText;
    @TableField(exist = false)
    private String agencyMobile;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 被绑定人
     */
    @NotNull(message = "绑定人不能为空！")
    private Long familyId;




}
