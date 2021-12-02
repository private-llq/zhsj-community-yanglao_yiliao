package com.zhsj.community.yanglao_yiliao.myself.job;

import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.UserSettingMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 事件提醒定时任务
 * @author: Hu
 * @create: 2021-11-15 11:24
 **/
@Component
public class EventJob {


    @Resource
    private EventMapper eventMapper;

    @Resource
    private UserSettingMapper userSettingMapper;


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
        List<EventEntity> list = eventMapper.selectByDay(now.getDayOfWeek().getValue(),now.getHour(),now.getMinute());
        if (list.size()!=0) {
            for (EventEntity entity : list) {

                //用户关闭了消息提醒   不晓得推不推消息
//                UserSettingEntity userSettingEntity = userSettingMapper.selectOne(new QueryWrapper<UserSettingEntity>().eq("uid", entity.getUid()));
//                if (userSettingEntity != null) {
//                    if (userSettingEntity.getNotificationStatus()==0){
//                        continue;
//                    }
//                }
                    //表示要提醒用户



                //修改事件提醒推送状态
                entity.setPushStatus(1);
                entity.setUpdateTime(LocalDateTime.now());
                eventMapper.updateById(entity);
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

        //修改所有不是单次提醒的提醒状态
        eventMapper.updateByStatus();
        System.out.println(LocalDateTime.now()+"：定时删除事件提醒完成！");
    }
}
