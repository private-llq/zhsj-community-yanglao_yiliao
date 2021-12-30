package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.utils.RedisUtils;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorSleepReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.SleepMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Sleep;
import com.zhsj.community.yanglao_yiliao.healthydata.service.SleepService;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户睡眠对应实体service层实现类
 * @date 2021/11/11 15:19
 */
@Slf4j
@Service
public class SleepServiceImpl extends ServiceImpl<SleepMapper, Sleep> implements SleepService {

    @Autowired
    private RedisUtils redisService;

    /***************************************************************************************************************************
     * @description 监控用户睡眠并保存
     * @author zzm
     * @date 2021/11/11 15:32
     * @param list 用户睡眠信息
     **************************************************************************************************************************/
    @Override
    public void monitorSleep(List<MonitorSleepReqBo> list) {
        log.info("Monitor user sleep request parameters, List<MonitorSleepReqBo> = {}", list);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("request parameter is empty, List<MonitorHeartRateReqBo> = {}", list);
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        HashSet<MonitorSleepReqBo> hashSet = new HashSet<>(list);
        List<Sleep> arr = new ArrayList<Sleep>();
        for (MonitorSleepReqBo reqBo : hashSet) {
            // ---排除历史重复数据
            Object beforeTime = redisService.get(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_SLEEP + loginUser.getAccount() + ":" + reqBo.getCreateTime());
            if (beforeTime != null) {
                continue;
            } else {
                redisService.set(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_SLEEP + loginUser.getAccount() + ":" + reqBo.getCreateTime(), reqBo.getCreateTime().toString(), 10L, TimeUnit.DAYS);
            }
            LocalDateTime localDateTime = TimeUtils.formatTimestamp(reqBo.getCreateTime());
//            Sleep sleep = getOne(new LambdaQueryWrapper<Sleep>()
//                    .eq(Sleep::getUserUuid, user.getAccount())
//                    .eq(Sleep::getCreateTime, localDateTime)
//                    .eq(Sleep::getDeleted, true));
//            if (sleep != null) {
//                continue;
//            }
            arr.add(Sleep.build(loginUser, reqBo, localDateTime));
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            saveBatch(arr);
        }
    }

    /***************************************************************************************************************************
     * @description 批量删除用户睡眠数据
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 睡眠id列表
     **************************************************************************************************************************/
    @Override
    public void batchDeleteSleep(List<Long> list) {
        log.info("Delete user sleep in batch request parameter, list = {}", list);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("Please check the body sleep to be deleted");
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        for (Long id : list) {
            Sleep sleep = getOne(new LambdaQueryWrapper<Sleep>()
                    .eq(Sleep::getId, id)
                    .eq(Sleep::getDeleted, true)
                    .eq(Sleep::getUserUuid, user.getAccount()));
            if (sleep == null) {
                log.error("Body sleep to delete not found, temperatureId = {}", id);
                throw new BaseException(ErrorEnum.SERVER_BUSY);
            }
        }
        removeByIds(list);
    }


}
