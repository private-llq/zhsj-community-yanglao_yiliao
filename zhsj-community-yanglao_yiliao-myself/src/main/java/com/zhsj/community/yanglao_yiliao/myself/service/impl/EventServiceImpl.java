package com.zhsj.community.yanglao_yiliao.myself.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.EventFamilyEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.EventStopEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventFamilyMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventStopMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
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

    @Resource
    private EventStopMapper eventStopMapper;



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
        eventEntity.setUid(loginUser.getAccount());
        eventEntity.setCreateTime(LocalDateTime.now());
        eventEntity.setWarnYear(eventEntity.getWarnTime().getYear());
        eventEntity.setWarnMonth(eventEntity.getWarnTime().getMonthValue());
        eventEntity.setWarnDay(eventEntity.getWarnTime().getDayOfMonth());
        eventEntity.setWarnWeek(eventEntity.getWarnTime().getDayOfWeek().getValue());
        eventMapper.insert(eventEntity);


        //添加事件提醒家人
        EventFamilyEntity entity;
        LinkedList<EventFamilyEntity> list = new LinkedList<>();
        for (Long family : eventEntity.getFamilies()) {
            entity = new EventFamilyEntity();
            entity.setFamilyId(family);
            entity.setId(SnowFlake.nextId());
            entity.setEventId(eventEntity.getId());
            entity.setUid(eventEntity.getUid());
            entity.setCreateTime(LocalDateTime.now());
            list.add(entity);
        }
        eventFamilyMapper.saveAll(list);
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
        eventEntity.setUid(loginUser.getAccount());
        eventEntity.setWarnYear(eventEntity.getWarnTime().getYear());
        eventEntity.setWarnMonth(eventEntity.getWarnTime().getMonthValue());
        eventEntity.setWarnDay(eventEntity.getWarnTime().getDayOfMonth());
        eventEntity.setWarnWeek(eventEntity.getWarnTime().getDayOfWeek().getValue());
        eventEntity.setUpdateTime(LocalDateTime.now());
        eventMapper.updateById(eventEntity);

        eventFamilyMapper.delete(new QueryWrapper<EventFamilyEntity>().eq("event_id",eventEntity.getId()).eq("uid",loginUser.getAccount()));

        EventFamilyEntity entity;
        LinkedList<EventFamilyEntity> list = new LinkedList<>();
        for (Long family : eventEntity.getFamilies()) {
            entity = new EventFamilyEntity();
            entity.setFamilyId(family);
            entity.setId(SnowFlake.nextId());
            entity.setEventId(eventEntity.getId());
            entity.setUid(eventEntity.getUid());
            entity.setCreateTime(LocalDateTime.now());
            list.add(entity);
        }
        eventFamilyMapper.saveAll(list);
    }


    /**
     * @Description: 根据时间查询详情
     * @author: Hu
     * @since: 2021/11/13 16:23
     * @Param: [localDate, loginUser]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>
     */
    @Override
    public List<EventEntity> list(LocalDate localDate, LoginUser loginUser) {
        Map<Long, EventStopEntity> map = new HashMap<>();
        Map<String, Object> paramMap;
        //封装所有关闭的事件提醒
        List<EventStopEntity> stopEntityList = eventStopMapper.selectList(new QueryWrapper<EventStopEntity>().eq("uid", loginUser.getAccount()));
        if (stopEntityList.size()!=0){
            for (EventStopEntity entity : stopEntityList) {
                map.put(entity.getEventId(),entity);
            }
        }
        List<EventEntity> entityList = eventMapper.selectByDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getDayOfWeek().getValue(), loginUser.getAccount());
        for (EventEntity entity : entityList) {
            //状态
            if (map.get(entity.getId())!=null) {
                entity.setStatus(0);
            }
            //封装提醒家人信息
            Set<Long> longSet = eventFamilyMapper.getByFamilyId(entity.getId());
            List<FamilyRecordEntity> entities = familyRecordMapper.selectBatchIds(longSet);
            if (entities.size()!=0){
                for (FamilyRecordEntity recordEntity : entities) {
                    paramMap = new HashMap<>(2);
                    paramMap.put("id",recordEntity.getId());
                    paramMap.put("name",recordEntity.getName());
                    entity.getRecords().add(paramMap);
                }
            }
        }
        return entityList;
    }

    @Override
    public List<EventEntity> pageList(LoginUser loginUser) {
        Map<Long, EventStopEntity> map = new HashMap<>();

        List<EventStopEntity> entities = eventStopMapper.selectList(new QueryWrapper<EventStopEntity>().eq("uid", loginUser.getAccount()));
        if (entities.size()!=0){
            for (EventStopEntity entity : entities) {
                map.put(entity.getEventId(),entity);
            }
        }
        LocalDate localDate = LocalDate.now();
        List<EventEntity> entityList = eventMapper.pageList(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), localDate.getDayOfWeek().getValue(), loginUser.getAccount());

        if (entityList.size()!=0){
            for (EventEntity eventEntity : entityList) {
                if (map.get(eventEntity.getId())!=null) {
                    eventEntity.setStatus(0);
                }
                if (eventEntity.getType()!=1){
                    eventEntity.setWarnTime(localDate);
                }
            }
        } else {
            entityList = new LinkedList<>();
            for (int i = 0; i < 3; i++) {
                EventEntity entity = new EventEntity();
                entity.setContent("这是一条引导数据！");
                entity.setStatus(1);
                entity.setWarnTime(LocalDate.now());
                entityList.add(entity);
            }
        }
        return entityList;

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


    /**
     * @Description: 启用停用
     * @author: Hu
     * @since: 2021/11/13 17:14
     * @Param: [status]
     * @return: void
     */
    @Override
    public void status(Long id,Integer status) {
        EventEntity eventEntity = eventMapper.selectById(id);
        if (eventEntity!=null){
            if (status==1){
                eventStopMapper.delete(new QueryWrapper<EventStopEntity>().eq("event_id",eventEntity.getId()).eq("uid",eventEntity.getUid()));
            } else {
                EventStopEntity eventStopEntity = new EventStopEntity();
                eventStopEntity.setEventId(id);
                eventStopEntity.setUid(eventEntity.getUid());
                eventStopEntity.setId(SnowFlake.nextId());
                eventStopEntity.setCreateTime(LocalDateTime.now());
                eventStopMapper.insert(eventStopEntity);
            }
        } else {
            throw new BaseException(ErrorEnum.COMMON_QUANTITY_LIMIT,"当前事件不存在！");
        }
    }
}
