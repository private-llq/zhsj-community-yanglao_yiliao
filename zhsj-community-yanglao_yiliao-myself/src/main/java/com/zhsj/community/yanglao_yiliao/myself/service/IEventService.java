package com.zhsj.community.yanglao_yiliao.myself.service;

import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;

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
    void list(Integer month, Integer day, LoginUser loginUser);
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
}
