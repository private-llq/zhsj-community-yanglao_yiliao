package com.zhsj.community.yanglao_yiliao.myself.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.EventFamilyEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.EventWeekEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.utils.SnowFlake;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventFamilyMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventWeekMapper;
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

    @Resource
    private EventWeekMapper eventWeekMapper;



    /**
     * @Description: 新增
     * @author: Hu
     * @since: 2021/11/12 15:42
     * @Param: [eventEntity, loginUser]
     * @return: void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(EventEntity eventEntity, LoginUser loginUser) {
        eventEntity.setId(SnowFlake.nextId());
        eventEntity.setUid(loginUser.getAccount());
        eventEntity.setCreateTime(LocalDateTime.now());
        eventMapper.insert(eventEntity);

        //添加事件提醒家人
        EventFamilyEntity entity;
        LinkedList<EventFamilyEntity> familyList = new LinkedList<>();
        for (Long family : eventEntity.getFamilies()) {
            entity = new EventFamilyEntity();
            entity.setFamilyId(family);
            entity.setId(SnowFlake.nextId());
            entity.setEventId(eventEntity.getId());
            entity.setUid(familyRecordMapper.selectById(family).getUid());
            entity.setCreateTime(LocalDateTime.now());
            familyList.add(entity);
        }
        eventFamilyMapper.saveAll(familyList);

        //添加事件周天
        EventWeekEntity eventWeekEntity;
        LinkedList<EventWeekEntity> weekList = new LinkedList<>();
        for (Integer week : eventEntity.getWeeks()) {
            eventWeekEntity = new EventWeekEntity();
            eventWeekEntity.setId(SnowFlake.nextId());
            eventWeekEntity.setEventId(eventEntity.getId());
            eventWeekEntity.setUid(loginUser.getAccount());
            eventWeekEntity.setWeek(week);
            weekList.add(eventWeekEntity);
        }
        eventWeekMapper.saveAll(weekList);
        return eventEntity.getId();
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
        eventEntity.setUpdateTime(LocalDateTime.now());
        eventMapper.updateById(eventEntity);

        eventFamilyMapper.delete(new QueryWrapper<EventFamilyEntity>().eq("event_id",eventEntity.getId()).eq("uid",loginUser.getAccount()));
        eventWeekMapper.delete(new QueryWrapper<EventWeekEntity>().eq("event_id",eventEntity.getId()).eq("uid",loginUser.getAccount()));
        //添加事件家人
        EventFamilyEntity entity;
        LinkedList<EventFamilyEntity> list = new LinkedList<>();
        for (Long family : eventEntity.getFamilies()) {
            entity = new EventFamilyEntity();
            entity.setFamilyId(family);
            entity.setId(SnowFlake.nextId());
            entity.setEventId(eventEntity.getId());
            entity.setUid(familyRecordMapper.selectById(family).getUid());
            entity.setCreateTime(LocalDateTime.now());
            list.add(entity);
        }
        eventFamilyMapper.saveAll(list);

        //添加事件周天
        EventWeekEntity eventWeekEntity;
        LinkedList<EventWeekEntity> weekList = new LinkedList<>();
        for (Integer week : eventEntity.getWeeks()) {
            eventWeekEntity = new EventWeekEntity();
            eventWeekEntity.setId(SnowFlake.nextId());
            eventWeekEntity.setEventId(eventEntity.getId());
            eventWeekEntity.setUid(loginUser.getAccount());
            eventWeekEntity.setWeek(week);
            weekList.add(eventWeekEntity);
        }
        eventWeekMapper.saveAll(weekList);
    }


    /**
     * @Description: 根据时间查询详情
     * @author: Hu
     * @since: 2021/11/13 16:23
     * @Param: [localDate, loginUser]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>
     */
    @Override
    public List<EventEntity> list(Integer week, LoginUser loginUser) {
        Map<String, Object> paramMap;
        List<EventEntity> entityList = eventMapper.selectByDate(week, loginUser.getAccount());
        for (EventEntity entity : entityList) {
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
            //封装事件周天
            List<EventWeekEntity> weekEntities = eventWeekMapper.selectList(new QueryWrapper<EventWeekEntity>().eq("event_id", entity.getId()));
            if (weekEntities.size()!=0) {
                for (EventWeekEntity weekEntity : weekEntities) {
                    entity.getWeeks().add(weekEntity.getWeek());
                }
            }
        }
        return entityList;
    }



    /**
     * @Description: 查询所有事件
     * @author: Hu
     * @since: 2021/12/3 13:48
     * @Param: [loginUser]
     * @return: java.util.List<com.zhsj.community.yanglao_yiliao.common.entity.EventEntity>
     */
    @Override
    public List<EventEntity> pageList(LoginUser loginUser) {
        Map<String, Object> paramMap;
        List<EventEntity> entityList = eventMapper.selectList(new QueryWrapper<EventEntity>().eq("uid",loginUser.getAccount()));
        for (EventEntity entity : entityList) {
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
            //封装事件周天
            List<EventWeekEntity> weekEntities = eventWeekMapper.selectList(new QueryWrapper<EventWeekEntity>().eq("event_id", entity.getId()));
            if (weekEntities.size()!=0) {
                for (EventWeekEntity weekEntity : weekEntities) {
                    entity.getWeeks().add(weekEntity.getWeek());
                }
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
        eventMapper.deleteById(id);
        eventFamilyMapper.delete(new QueryWrapper<EventFamilyEntity>().eq("event_id",id));
        eventWeekMapper.delete(new QueryWrapper<EventWeekEntity>().eq("event_id",id));
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
        List<EventWeekEntity> weekEntities = eventWeekMapper.selectList(new QueryWrapper<EventWeekEntity>().eq("event_id", id));
        if (weekEntities.size()!=0) {
            for (EventWeekEntity weekEntity : weekEntities) {
                entity.getWeeks().add(weekEntity.getWeek());
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
            eventEntity.setStatus(status);
            eventMapper.updateById(eventEntity);
        } else {
            throw new BaseException(ErrorEnum.COMMON_QUANTITY_LIMIT,"当前事件不存在！");
        }
    }
}
