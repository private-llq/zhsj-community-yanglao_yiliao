package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.utils.RedisUtils;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorTemperatureReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.TemperatureMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Temperature;
import com.zhsj.community.yanglao_yiliao.healthydata.service.TemperatureService;
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
 * @Description: 用户温度service层对应实现
 * @date 2021/11/11 13:57
 */
@Slf4j
@Service
public class TemperatureServiceImpl extends ServiceImpl<TemperatureMapper, Temperature> implements TemperatureService {

    @Autowired
    private RedisUtils redisService;

    /***************************************************************************************************************************
     * @description 检测用户体温并保存
     * @author zzm
     * @date 2021/11/11 14:08
     * @param list 用户体温信息
     **************************************************************************************************************************/
    @Override
    public void monitorTemperature(List<MonitorTemperatureReqBo> list) {
        log.info("检测用户体温并保存,List<MonitorTemperatureReqBo> = {}", list);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (CollectionUtil.isEmpty(list)) {
            log.error("request parameter is empty, List<MonitorHeartRateReqBo> = {}", list);
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        HashSet<MonitorTemperatureReqBo> hashSet = new HashSet<>(list);
        List<Temperature> arr = new ArrayList<Temperature>();
        for (MonitorTemperatureReqBo reqBo : hashSet) {
            LocalDateTime time = TimeUtils.formatTimestamp(reqBo.getCreateTime());
            String s = TimeUtils.formatTime(time);
            // ---排除历史重复数据
            Object beforeTime = redisService.get(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_TEMP + loginUser.getAccount() + ":" + s);
            if (beforeTime != null) {
                continue;
            } else {
                redisService.set(HealthDataConstant.HEALTH_DATA_REMOVE_REPEAT_TEMP + loginUser.getAccount() + ":" + s, s, 10L, TimeUnit.DAYS);
            }
            arr.add(Temperature.build(loginUser, reqBo, time));
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            saveBatch(arr);
        }
    }

    /***************************************************************************************************************************
     * @description 批量删除用户体温数据（预留大后台）
     * @author zzm
     * @date 2021/11/11 17:19
     * @param list 体温id列表
     **************************************************************************************************************************/
    @Override
    public void batchDeleteTemperature(List<Long> list) {
        log.info("批量删除用户体温数据, list = {}", list);
        if (CollectionUtil.isEmpty(list)) {
            log.error("参数不能为空");
            throw new BaseException(ErrorEnum.PARAMS_ERROR);
        }
        List<Long> arr = new ArrayList<>();
        for (Long id : list) {
            Temperature temperature = getOne(new LambdaQueryWrapper<Temperature>()
                    .eq(Temperature::getId, id)
                    .eq(Temperature::getDeleted, true));
            if (temperature == null) {
                log.error("要删除的体温数据不存在, id = {}", id);
                continue;
            }
            arr.add(id);
        }
        if (CollectionUtil.isNotEmpty(arr)) {
            removeByIds(list);
        }
    }
}
