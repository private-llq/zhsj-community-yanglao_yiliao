package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private String agencyId;
    /**
     * 用户uid
     */
    private String uid;

    /**
     * 被绑定人
     */
    private Long familyId;


}
