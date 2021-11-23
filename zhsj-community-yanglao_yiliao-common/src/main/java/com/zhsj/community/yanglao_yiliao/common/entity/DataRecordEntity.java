package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 健康数据档案
 * @author: Hu
 * @create: 2021-11-22 14:43
 **/
@Data
@TableName("t_data_record")
public class DataRecordEntity extends BaseEntity {
    /**
     * 内容
     */
    private String content;
    /**
     * 父级id
     */
    private Long pid;

    /**
     * 子集菜单
     */
    @TableField(exist = false)
    private List<DataRecordEntity> submenu = new LinkedList<>();

}
