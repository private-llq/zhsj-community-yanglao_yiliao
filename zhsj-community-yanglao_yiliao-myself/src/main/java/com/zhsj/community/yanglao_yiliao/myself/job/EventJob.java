package com.zhsj.community.yanglao_yiliao.myself.job;

import cn.hutool.core.collection.CollectionUtil;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.base.api.vo.UserImVo;
import com.zhsj.community.yanglao_yiliao.common.entity.EventEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventFamilyMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.EventMapper;
import com.zhsj.im.chat.api.appmsg.impl.TextAppMsg;
import com.zhsj.im.chat.api.rpc.IImChatPublicPushRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @program: zhsj-community-yanglao_yiliao t
 * @description: 事件提醒定时任务
 * @author: Hu
 * @create: 2021-11-15 11:24
 **/
@Slf4j
@Component
public class EventJob {

    @Resource
    private EventMapper eventMapper;
    @Resource
    private EventFamilyMapper eventFamilyMapper;
    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBaseUserInfoRpcService userInfoRpcService;
    @DubboReference(version = com.zhsj.im.chat.api.constant.RpcConst.Rpc.VERSION, group = com.zhsj.im.chat.api.constant.RpcConst.Rpc.Group.GROUP_IM_CHAT, check = false)
    private IImChatPublicPushRpcService iImChatPublicPushRpcService;

    /**
     * dd
     *
     * @Description: 每分钟执行  事件提醒
     * @author: Hu
     * @since: 2021/11/15 11:24
     * @Param: []
     * @return: void
     */
    @Scheduled(cron = "0 * * * * ?")
    public void event() {
        LocalDateTime now = LocalDateTime.now();
        List<EventEntity> list = eventMapper.selectByDay(now.getDayOfWeek().getValue(), now.getHour(), now.getMinute());
        if (CollectionUtil.isNotEmpty(list)) {
            for (EventEntity entity : list) {
                Set<String> user = eventFamilyMapper.selectByUser(entity.getId());
                for (String uid : user) {
                    //推送消息
                    UserImVo userIm = userInfoRpcService.getEHomeUserIm(uid);
                    if (userIm == null || userIm.getImId() == null) {
                        log.error("调用【IBaseUserInfoRpcService】的【getEHomeUserIm】获取【E到家用户imid】为null，uid = {},UserImVo = {}", uid, userIm);
                        continue;
                    }
                    iImChatPublicPushRpcService.sendMessage(new TextAppMsg("事件提醒",
                            entity.getContent(),
                            "",
                            "",
                            entity.getContent(),
                            null,
                            "sysMessage",
                            1,
                            userIm.getImId(),
                            null));
                }
                //修改事件提醒推送状态
                entity.setPushStatus(1);
                entity.setUpdateTime(LocalDateTime.now());
                eventMapper.updateById(entity);
            }
            System.out.println(now + "：----------事件提醒处理完成！----------");
        }
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
        System.out.println(LocalDateTime.now() + "：定时删除事件提醒完成！");
    }
}
