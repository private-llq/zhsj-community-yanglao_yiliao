package com.zhsj.community.yanglao_yiliao.old_activity.common;

import lombok.Data;

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
    private Integer page = 1;
    /**
     *  每页显示多少条
     */
    private Integer data = 10;
}
