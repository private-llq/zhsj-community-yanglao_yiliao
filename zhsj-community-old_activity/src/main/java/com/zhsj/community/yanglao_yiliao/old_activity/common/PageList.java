package com.zhsj.community.yanglao_yiliao.old_activity.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 分页
 * @create: 2021-11-16 15:15
 */

/**
 * 分页对象：easyui只需两个属性，total(总数),datas（分页数据）就能实现分页
 */
public class PageList<T> {
    /**
     * 总条数
     */
    private Long total = 0L;
    private List<T> data = new ArrayList<T>();
    /**
     * 当前页
     */
    private long current = 1;

    public long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> rows) {
        this.data = rows;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "PageList{" +
                "total=" + total +
                ", data=" + data +
                '}';
    }

    //提供有参构造方法，方便测试
    public PageList(Long total, List<T> data) {
        this.total = total;
        this.data = data;
    }

    //除了有参构造方法，还需要提供一个无参构造方法
    public PageList() {
    }

}
