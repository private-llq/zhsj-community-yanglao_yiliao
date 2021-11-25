package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private Long dataRecordId;
    /**
     * 描述
     */
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
}
