package com.zhsj.community.yanglao_yiliao.old_activity.common;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2021-11-26 16:02
 */
@Data
public class pageVoed {
    /**
     *  当前页
     */
    @NotNull(message = "当前页不能为空")
    private Integer page ;
    /**
     *  每页显示多少条
     */
    @NotNull(message = "条数不能为空")
    private Integer data ;
}
