package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案
 * @author: Hu
 * @create: 2021-11-10 11:39
 **/
@Data
@TableName("t_family_record")
public class FamilyRecordEntity extends BaseEntity {
    /**
     * 关系
     */
    private Integer relation;
    /**
     * 关系文本
     */
    @TableField(exist = false)
    private String relationText;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 用户uid
     */
    private String uid;

}
