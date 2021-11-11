package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorSleepReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.SleepMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Sleep;
import com.zhsj.community.yanglao_yiliao.healthydata.service.SleepService;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户睡眠对应实体service层实现类
 * @date 2021/11/11 15:19
 */
@Slf4j
@Service
public class SleepServiceImpl extends ServiceImpl<SleepMapper, Sleep> implements SleepService {

    /***************************************************************************************************************************
     * @description 监控用户睡眠并保存
     * @author zzm
     * @date 2021/11/11 15:32
     * @param list 用户睡眠信息
     **************************************************************************************************************************/
    @Override
    public void monitorSleep(List<MonitorSleepReqBo> list) {
        log.info("Monitor user sleep request parameters, List<MonitorSleepReqBo> = {}", list);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        List<Sleep> arr = new ArrayList<Sleep>();
        for (MonitorSleepReqBo reqBo : list) {
            LocalDateTime localDateTime = TimeUtils.formatTimestamp(reqBo.getCreateTime());
            Sleep sleep = getOne(new LambdaQueryWrapper<Sleep>()
                    .eq(Sleep::getUserUuid, user.getAccount())
                    .eq(Sleep::getCreateTime, localDateTime)
                    .eq(Sleep::getDeleted, true));
            if (sleep != null) {
                continue;
            }
            arr.add(Sleep.build(user, reqBo, localDateTime));
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            saveBatch(arr);
        }
    }
}
