package com.zhsj.community.yanglao_yiliao.old_activity.util;



import com.zhsj.community.yanglao_yiliao.old_activity.exception.BootException;

import java.util.List;

/**
 * 手动分页工具类
 * liin
 */
public class MyPageUtils {
    /**
     * @param pageNo   第几页 （从1计数）
     * @param pageSize 每页展示几条数据
     * @param data     数据源
     * @return 分页对象
     */
    public static <T> PageInfo<T> pageMap(int pageNo, int pageSize, List<T> data) {
        PageInfo<T> pageInfo = new PageInfo<>();
        int[] ints = transToStartEnd(pageNo, pageSize, data.size());
        List<T> list = data.subList(ints[0], ints[1]);
        pageInfo.setdata(list);
        pageInfo.setTotal(data.size());
        pageInfo.setCurrent(pageNo);
        pageInfo.setSize(pageSize);
        return pageInfo;
    }
    /**
     *
     * @param pageNo 第几页 （从1计数）
     * @param pageSize 每页展示几条数据
     * @param dataSize 数据源元素个数
     * @return 第一个开始位置 第二个结束位置
     */
    private static int[] transToStartEnd(int pageNo, int pageSize, int dataSize) {
        if (pageNo < 1) {
            pageNo = 1;
        }

        if (pageSize < 1) {
            pageSize = 0;
        }
        final int start=  (pageNo - 1) * pageSize;
        if (pageSize < 1) {
            pageSize = 0;
        }
        int end = pageNo * pageSize;
        if (end > dataSize) {
            end = dataSize;
        }
        if (start > end) {
            throw new BootException("页码错误,重新输入");
        }
        return new int[] { start, end };
    }
}