package com.zhsj.community.yanglao_yiliao.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 分页响应数据
 * @author zzm
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PageVo<T> implements Serializable {

    /**
     * 当前页
     */
    private Long pageNo;

    /**
     * 每页显示条数
     */
    private Long pageSize;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 总数据条数
     */
    private Long total;

    /**
     * 分页数据
     */
    private List<T> list;

    /**
     * MyBatis-Plus Page对象转换为PageVO
     */
    public static <T> PageVo<T> newPageVO(Page<T> page) {
        return new PageVo<T>()
                .setPageNo(page.getCurrent())
                .setPageSize(page.getSize())
                .setPages(page.getPages())
                .setTotal(page.getTotal())
                .setList(CollectionUtil.isEmpty(page.getRecords()) ? CollectionUtil.newArrayList() : page.getRecords());
    }

    public PageVo() {
        this.list = new ArrayList<>();
        this.pages = 0L;
        this.pageNo = 0L;
        this.pageSize = 0L;
        this.total = 0L;
    }

    public PageVo(Long pageNo, Long pageSize, Long pages, Long total, List<T> list) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
        this.list = list;
    }
}