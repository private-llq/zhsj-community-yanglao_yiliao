package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos家属信息
 * @author: Hu
 * @create: 2021-11-11 14:01
 **/
@Data
@TableName("t_family_sos")
public class FamilySosEntity extends BaseEntity {
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空！",groups = {SosValidate.class})
    private String name;
    /**
     * 电话
     */
    @NotBlank(message = "姓名不能为空！",groups = {SosValidate.class})
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]{9}$", message = "请输入一个正确的手机号码 电信丨联通丨移动!",groups = {SosValidate.class})
    private String mobile;
    /**
     * 用户uid
     */
    private String uid;


    /**
     * 被绑定人
     */
    @NotNull(message = "被绑定人不能为空！",groups = {SosValidate.class})
    private Long familyId;

    public interface SosValidate{}
}
