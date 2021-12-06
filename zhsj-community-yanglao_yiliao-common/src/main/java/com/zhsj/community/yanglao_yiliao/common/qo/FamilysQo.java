package com.zhsj.community.yanglao_yiliao.common.qo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 家人档案导入接收类
 * @author: Hu
 * @create: 2021-12-02 16:16
 **/
@Data
public class FamilysQo implements Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 电话
     */
    private String mobile;

    /**
     * 接收社区家人数据集合
     */
    @NotEmpty(message = "导入家人不能为空！")
    private List<FamilysQo> families  = new LinkedList<>();
}
