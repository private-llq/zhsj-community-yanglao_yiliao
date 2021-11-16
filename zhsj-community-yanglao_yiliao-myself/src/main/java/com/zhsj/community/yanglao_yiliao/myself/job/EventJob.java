package com.zhsj.community.yanglao_yiliao.myself.job;

import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.EventStopEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventStopMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒定时任务
 * @author: Hu
 * @create: 2021-11-15 11:24
 **/
@Component
public class EventJob {

    @Resource
    private EventStopMapper eventStopMapper;

    @Resource
    private EventMapper eventMapper;


    /**
     * @Description: 每分钟执行  事件提醒
     * @author: Hu
     * @since: 2021/11/15 11:24
     * @Param: []
     * @return: void
     */
    @Scheduled(cron = "0 * * * * ?")
    public void event() {
        LocalDateTime now = LocalDateTime.now();
        Map<Long, EventStopEntity> map = new HashMap<>();
        List<EventStopEntity> entities = eventStopMapper.selectList(null);
        for (EventStopEntity entity : entities) {
            map.put(entity.getEventId(),entity);
        }
        List<EventEntity> list = eventMapper.selectByDay(now.getYear(),now.getMonthValue(),now.getDayOfWeek().getValue(),now.getDayOfMonth(),now.getHour(),now.getMinute());
        if (list.size()!=0) {
            for (EventEntity entity : list) {
                if (map.get(entity.getId())==null){
                    //表示要提醒用户



                }
                //type=1表示单次提醒  提醒完了直接删除
                if (entity.getType()==1){
                    eventMapper.deleteById(entity.getId());
                }
            }
        }
        System.out.println(now+"：定时处理事件提醒完成！");

    }

    /**
     * @Description: 每天0点触发
     * @author: Hu
     * @since: 2021/11/15 11:24
     * @Param: []
     * @return: void
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void reset() {
        //删除所有当天不提醒的所有事件记录
        eventStopMapper.delete(null);
        System.out.println(LocalDateTime.now()+"：定时删除事件提醒完成！");
    }
}
