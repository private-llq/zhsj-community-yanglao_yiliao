package com.zhsj.community.yanglao_yiliao.myself.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒
 * @author: Hu
 * @create: 2021-11-12 14:43
 **/
public interface EventMapper extends BaseMapper<EventEntity> {


    /**
     * @Description: 按天查列表
     * @author: Hu
     * @since: 2021/11/13 16:30
     * @Param:
     * @return:
     */
    List<EventEntity> selectByDate(@Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("week") int week, @Param("uid") String uid);
}
