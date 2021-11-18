package com.zhsj.community.yanglao_yiliao.old_activity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 分页
 * @create: 2021-11-16 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {
    /**
     * 总记录数
     */
    private Integer counts ;
    /**
     * 页大小
     */
    private Integer pagesize;
    /**
     * 总页数
     */
    private Integer pages ;
    /**
     * 当前页码
     */
    private Integer page;
    /**
     * 列表
     */
    private List<?> items = Collections.emptyList();

}
