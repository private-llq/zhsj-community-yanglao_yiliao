package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.utils.RedisUtils;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorHeartRateReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.HeartRateMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.HeartRate;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HeartRateService;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户心率service实现层
 * @date 2021/11/11 9:24
 */
@Slf4j
@Service
@EnableScheduling
public class HeartRateServiceImpl extends ServiceImpl<HeartRateMapper, HeartRate> implements HeartRateService {

    @Autowired
    private RedisUtils redisService;

    /***************************************************************************************************************************
     * @description 监测用户实时心率以及历史心率并保存（历史心率需筛选）
     * @author zzm
     * @date 2021/11/11 9:34
     * @param list 心率信息
     **************************************************************************************************************************/
    @Override
    public void monitorHeartRate(List<MonitorHeartRateReqBo> list) {
        log.info("监测用户实时心率以及历史心率并保存请求参数,List<MonitorHeartRateReqBo> = {}", list);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("请求参数不能为空, List<MonitorHeartRateReqBo> = {}", list);
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        HashSet<MonitorHeartRateReqBo> hashSet = new HashSet<>(list);
        List<HeartRate> arr = new ArrayList<>();
        for (MonitorHeartRateReqBo reqBo : hashSet) {
            LocalDateTime time = TimeUtils.formatTimestamp(reqBo.getCreateTime());
            String s = TimeUtils.formatTime(time);
            // ---排除历史重复数据
            Object beforeTime = redisService.get(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_HEART_RATE + loginUser.getAccount() + ":" + s);
            if (beforeTime != null) {
                continue;
            } else {
                redisService.set(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_HEART_RATE + loginUser.getAccount() + ":" + s, s, 10L, TimeUnit.DAYS);
            }
            arr.add(HeartRate.build(loginUser, reqBo, time));
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            saveBatch(arr);
        }
    }

    /***************************************************************************************************************************
     * @description 批量删除用户心率数据（预留大后台）
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 心率id列表
     **************************************************************************************************************************/
    @Override
    public void batchDeleteHeartRate(List<Long> list) {
        log.info("批量删除用户心率数据, list = {}", list);
        if (CollectionUtil.isEmpty(list)) {
            log.error("请求参数不能为空");
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        List<Long> arr = new ArrayList<>();
        for (Long id : list) {
            HeartRate heartRate = getOne(new LambdaQueryWrapper<HeartRate>()
                    .eq(HeartRate::getId, id)
                    .eq(HeartRate::getDeleted, true));
            if (heartRate == null) {
                log.error("要删除的心率不存在, id = {}", id);
                continue;
            }
            arr.add(id);
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            removeByIds(arr);
        }
    }

}
