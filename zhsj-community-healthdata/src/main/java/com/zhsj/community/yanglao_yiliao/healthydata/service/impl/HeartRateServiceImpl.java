package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorHeartRateReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.mapper.HeartRateMapper;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.HeartRate;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HeartRateService;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.SQLOutput;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户心率service实现层
 * @date 2021/11/11 9:24
 */
@Slf4j
@Service
public class HeartRateServiceImpl extends ServiceImpl<HeartRateMapper, HeartRate> implements HeartRateService {

    /***************************************************************************************************************************
     * @description 监测用户实时心率以及历史心率并保存（历史心率需筛选）
     * @author zzm
     * @date 2021/11/11 9:34
     * @param list 心率信息
     **************************************************************************************************************************/
    @Override
    public void monitorHeartRate(List<MonitorHeartRateReqBo> list) {
        log.info("Real time monitoring of user heart rate parameters,List<MonitorHeartRateReqBo> = {}", list);
        LoginUser loginUser = isAuth();
        List<HeartRate> arr = new ArrayList<>();
        for (MonitorHeartRateReqBo reqBo : list) {
            HeartRate heartRate = getOne(new LambdaQueryWrapper<HeartRate>()
                    .eq(HeartRate::getUserUuid, loginUser.getAccount())
                    .eq(HeartRate::getCreateTime, TimeUtils.formatTimestamp(reqBo.getCreateTime())));
            if (heartRate != null) {
                continue;
            }
            arr.add(HeartRate.build(loginUser, reqBo));
        }
        saveBatch(arr);
    }


    // ------------------------------------------------inner-----------------------------------------------------------------

    /**
     * 获取当前登录用户
     */
    private LoginUser isAuth() {
        return ContextHolder.getContext().getLoginUser();
    }
}
