package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "内容不能为空！！",groups = {SaveDataRecordValidate.class})
    private String content;
    /**
     * 父级id
     */
    private Long pid;

    /**
     * 1b端，2c端
     */
    @NotNull(message = "端点不能为空！",groups = {DataRecordValidate.class,SaveDataRecordValidate.class})
    @Range(min = 1,max = 2, message = "端点不存在",groups = {DataRecordValidate.class,SaveDataRecordValidate.class})
    private Integer site;
    /**
     * 1便民生活需求定制，2美食需求定制，3休闲娱乐需求定制，4社区医疗需求定制
     */
    @NotNull(message = "类型不能为空！",groups = {DataRecordValidate.class,SaveDataRecordValidate.class})
    @Range(min = 1,max = 4, message = "类型不存在！",groups = {DataRecordValidate.class,SaveDataRecordValidate.class})
    private Integer type;


    /**
     * 子集菜单
     */
    @TableField(exist = false)
    private List<DataRecordEntity> submenu = new LinkedList<>();

    public interface DataRecordValidate{}

    public interface SaveDataRecordValidate{}

}
