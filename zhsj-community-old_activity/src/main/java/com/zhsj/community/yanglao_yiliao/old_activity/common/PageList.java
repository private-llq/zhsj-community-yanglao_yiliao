package com.zhsj.community.yanglao_yiliao.old_activity.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
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
@ToString
//分页对象：easyui只需两个属性，total(总数),datas（分页数据）就能实现分页
public class PageList<T> {
    private Long total=0L;
    private List<T> rows = new ArrayList<T>();

    public long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }


}
