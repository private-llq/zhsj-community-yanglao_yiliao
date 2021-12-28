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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
        log.info("Real time monitoring of user heart rate parameters,List<MonitorHeartRateReqBo> = {}", list);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("request parameter is empty, List<MonitorHeartRateReqBo> = {}", list);
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        HashSet<MonitorHeartRateReqBo> hashSet = new HashSet<>(list);
        List<HeartRate> arr = new ArrayList<>();
        for (MonitorHeartRateReqBo reqBo : hashSet) {
            // ---排除历史重复数据
            Object beforeTime = redisService.get(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_HEART_RATE + loginUser.getAccount() + ":" + reqBo.getCreateTime());
            if (beforeTime != null) {
                continue;
            } else {
                redisService.set(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_HEART_RATE + loginUser.getAccount() + ":" + reqBo.getCreateTime(), reqBo.getCreateTime(), 10L, TimeUnit.DAYS);
            }
            LocalDateTime localDateTime = TimeUtils.formatTimestamp(reqBo.getCreateTime());
//            HeartRate heartRate = getOne(new LambdaQueryWrapper<HeartRate>()
//                    .eq(HeartRate::getUserUuid, loginUser.getAccount())
//                    .eq(HeartRate::getCreateTime, localDateTime)
//                    .eq(HeartRate::getDeleted, true));
//            if (heartRate != null) {
//                continue;
//            }
            arr.add(HeartRate.build(loginUser, reqBo, localDateTime));
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            saveBatch(arr);
        }
    }

    /***************************************************************************************************************************
     * @description 批量删除用户心率数据
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 心率id列表
     **************************************************************************************************************************/
    @Override
    public void batchDeleteHeartRate(List<Long> list) {
        log.info("Delete user heartRate in batch request parameter, list = {}", list);
        LoginUser user = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("Please check the body heartRate to be deleted");
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        for (Long id : list) {
            HeartRate heartRate = getOne(new LambdaQueryWrapper<HeartRate>()
                    .eq(HeartRate::getId, id)
                    .eq(HeartRate::getDeleted, true)
                    .eq(HeartRate::getUserUuid, user.getAccount()));
            if (heartRate == null) {
                log.error("Body heartRate to delete not found, temperatureId = {}", id);
                throw new BaseException(ErrorEnum.SERVER_BUSY);
            }
        }
        removeByIds(list);
    }

}
