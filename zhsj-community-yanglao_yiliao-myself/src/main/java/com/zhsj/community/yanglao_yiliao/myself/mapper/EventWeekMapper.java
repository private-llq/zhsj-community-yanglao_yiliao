package com.zhsj.community.yanglao_yiliao.myself.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.common.entity.EventWeekEntity;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件停用
 * @author: Hu
 * @create: 2021-11-13 14:53
 **/
public interface EventWeekMapper extends BaseMapper<EventWeekEntity> {

    /**
     * @Description: 批量新增
     * @author: Hu
     * @since: 2021/12/1 14:19
     * @Param:
     * @return:
     */
    void saveAll(@Param("list") LinkedList<EventWeekEntity> weekList);
}
