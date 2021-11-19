package com.zhsj.community.yanglao_yiliao.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    @Range(min = 1,max = 5, message = "关系不存在",groups = {UpdateFamilyValidate.class})
    private Integer relation;
    /**
     * 关系文本
     */
    @TableField(exist = false)
    private String relationText;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空！",groups = {UpdateFamilyValidate.class,AddFamilyValidate.class})
    private String name;
    /**
     * 电话
     */
    @NotBlank(message = "姓名不能为空！",groups = {UpdateFamilyValidate.class,AddFamilyValidate.class})
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]{9}$", message = "请输入一个正确的手机号码 电信丨联通丨移动!",groups = {UpdateFamilyValidate.class,AddFamilyValidate.class})
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
    @Pattern(regexp = "(^\\d{18}$)|(^\\d{15}$)", message = "请输入一个正确身份证号码",groups = {UpdateFamilyValidate.class})
    private String idCard;
    /**
     * 用户uid
     */
    private String uid;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 信息完善状态0未完善，1已完善
     */
    @TableField(exist = false)
    private Integer status;

    /**
     * 验证码
     */
    @TableField(exist = false)
    @NotBlank(message = "姓名不能为空！",groups = {AddFamilyValidate.class})
    private String code;

    public interface UpdateFamilyValidate{}

    public interface AddFamilyValidate{}

}
