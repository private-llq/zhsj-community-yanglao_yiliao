package com.zhsj.community.yanglao_yiliao.myself.service;

import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒
 * @author: Hu
 * @create: 2021-11-12 14:44
 **/
public interface IEventService {
    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/12 14:57
     * @Param:
     * @return:
     */
    void save(EventEntity eventEntity, LoginUser loginUser);
    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/11/12 14:57
     * @Param:
     * @return:
     */
    void update(EventEntity eventEntity, LoginUser loginUser);
    /**
     * @Description: 查集合
     * @author: Hu
     * @since: 2021/11/12 14:57
     * @Param:
     * @return:
     */
    List<EventEntity> list(LocalDate localDate, LoginUser loginUser);
    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/11/12 14:57
     * @Param:
     * @return:
     */
    void delete(Long id);
    /**
     * @Description: 查详情
     * @author: Hu
     * @since: 2021/11/12 14:57
     * @Param:
     * @return:
     */
    EventEntity getById(Long id);

    /**
     * @Description: 启用停用
     * @author: Hu
     * @since: 2021/11/13 17:14
     * @Param:
     * @return:
     */
    void status(Long id,Integer status);

    /**
     * @Description: 分页查列表
     * @author: Hu
     * @since: 2021/11/15 10:37
     * @Param:
     * @return:
     */
    List<EventEntity> pageList(LoginUser loginUser);
}
