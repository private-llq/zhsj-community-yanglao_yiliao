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
    List<EventEntity> selectByDate(@Param("week") int week, @Param("uid") String uid);

    /**
     * @Description: 分页查询
     * @author: Hu
     * @since: 2021/11/15 10:40
     * @Param:
     * @return:
     */
    List<EventEntity> pageList(@Param("year") int year, @Param("month") int monthValue, @Param("day") int dayOfMonth, @Param("week") int week, @Param("uid") String uid);

    /**
     * @Description: 所有当前分钟需要提醒的事件
     * @author: Hu
     * @since: 2021/11/15 14:51
     * @Param:
     * @return:
     */
    List<EventEntity> selectByDay(@Param("week") int week,@Param("hour") int hour, @Param("minute") int minute);

    /**
     * @Description: 修改事件提醒状态
     * @author: Hu
     * @since: 2021/11/26 14:35
     * @Param:
     * @return:
     */
    void updateByStatus();
}
