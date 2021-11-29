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
    private Integer page = 1; //当前页
    private Integer rows = 10; //每页显示多少条
}
