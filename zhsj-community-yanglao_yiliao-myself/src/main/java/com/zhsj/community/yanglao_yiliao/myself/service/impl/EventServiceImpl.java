package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.EventFamilyEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventFamilyMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒
 * @author: Hu
 * @create: 2021-11-12 14:46
 **/
@Service
public class EventServiceImpl implements IEventService {

    @Resource
    private FamilyRecordMapper familyRecordMapper;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private EventFamilyMapper eventFamilyMapper;



    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/12 15:42
     * @Param: [eventEntity, loginUser]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EventEntity eventEntity, LoginUser loginUser) {
        eventEntity.setId(SnowFlake.nextId());
        eventEntity.setUid(loginUser.getCurrentIp());
        eventEntity.setCreateTime(LocalDateTime.now());
        eventMapper.insert(eventEntity);

        List<EventFamilyEntity> families = eventEntity.getFamilies();
        if (families.size()!=0){
            for (EventFamilyEntity family : families) {
                family.setId(SnowFlake.nextId());
                family.setEventId(eventEntity.getId());
                family.setUid(eventEntity.getUid());
                family.setCreateTime(LocalDateTime.now());
            }
            eventFamilyMapper.saveAll(families);
        }
    }


    /**
     * @Description: 修改
     * @author: Hu
     * @since: 2021/11/12 15:42
     * @Param: [eventEntity, loginUser]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EventEntity eventEntity, LoginUser loginUser) {
        eventEntity.setUpdateTime(LocalDateTime.now());
        eventMapper.updateById(eventEntity);

        eventFamilyMapper.delete(new QueryWrapper<EventFamilyEntity>().eq("event_id",eventEntity.getId()).eq("uid",loginUser.getCurrentIp()));
        List<EventFamilyEntity> families = eventEntity.getFamilies();
        if (families.size()!=0){
            for (EventFamilyEntity family : families) {
                family.setId(SnowFlake.nextId());
                family.setEventId(eventEntity.getId());
                family.setUid(eventEntity.getUid());
                family.setCreateTime(LocalDateTime.now());
            }
            eventFamilyMapper.saveAll(families);
        }
    }

    @Override
    public void list(Integer month, Integer day, LoginUser loginUser) {
        eventMapper.selectList(new QueryWrapper<EventEntity>().eq("warn_day",day).eq("warn_hour",month));
    }


    /**
     * @Description: 删除
     * @author: Hu
     * @since: 2021/11/12 15:41
     * @Param: [id]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        eventFamilyMapper.delete(new QueryWrapper<EventFamilyEntity>().eq("event_id",id));
        eventMapper.deleteById(id);
    }


    /**
     * @Description: 查详情
     * @author: Hu
     * @since: 2021/11/12 15:41
     * @Param: [id]
     * @return: com.zhsj.community.yanglao_yiliao.common.entity.EventEntity
     */
    @Override
    public EventEntity getById(Long id) {
        Map<String, Object> paramMap;
        EventEntity entity = eventMapper.selectById(id);
        Set<Long> ids = eventFamilyMapper.getByFamilyId(id);
        List<FamilyRecordEntity> entities = familyRecordMapper.selectBatchIds(ids);
        if (entities.size()!=0){
            for (FamilyRecordEntity recordEntity : entities) {
                paramMap = new HashMap<>(2);
                paramMap.put("id",recordEntity.getId());
                paramMap.put("name",recordEntity.getName());
                entity.getRecords().add(paramMap);
            }
        }
        return entity;
    }
}
