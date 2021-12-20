package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 用户健康信息
 * @author: Hu
 * @create: 2021-11-22 14:49
 **/
@Data
@TableName("t_user_data_record")
public class UserDataRecordEntity extends BaseEntity {
    /**
     * 健康数据id
     */
    @NotNull(message = "数据id不能为空！")
    private Long dataRecordId;
    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空！")
    private String description;
    /**
     * 用户id
     */
    private String uid;

    /**
     * 子集类容
     */
    @TableField(exist = false)
    private String sonContent;

    /**
     * 父级类容
     */
    @TableField(exist = false)
    private String sireContent;

    /**
     * 1b端，2c端
     */
    @NotNull(message = "端点不能为空！")
    @Range(min = 1,max = 2, message = "端点不存在")
    private Integer site;
    /**
     * 1便民生活需求定制，2美食需求定制，3休闲娱乐需求定制，4社区医疗需求定制
     */
    @NotNull(message = "类型不能为空！")
    @Range(min = 1,max = 4, message = "类型不存在！")
    private Integer type;
}
