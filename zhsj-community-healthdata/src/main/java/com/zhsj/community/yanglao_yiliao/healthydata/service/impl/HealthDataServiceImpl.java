package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataRspBo;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.HeartRate;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Sleep;
import com.zhsj.community.yanglao_yiliao.healthydata.pojo.Temperature;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HealthDataService;
import com.zhsj.community.yanglao_yiliao.healthydata.service.HeartRateService;
import com.zhsj.community.yanglao_yiliao.healthydata.service.SleepService;
import com.zhsj.community.yanglao_yiliao.healthydata.service.TemperatureService;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zzm
 * @version 1.0
 * @Description: 健康数据页面实时健康数据service实现层
 * @date 2021/11/12 13:51
 */
@Slf4j
@Service
public class HealthDataServiceImpl implements HealthDataService {

    @Autowired
    private HeartRateService heartRateService;
    @Autowired
    private SleepService sleepService;
    @Autowired
    private TemperatureService temperatureService;

    /***************************************************************************************************************************
     * @description 获取用户实时健康数据
     * @author zzm
     * @date 2021/11/12 14:13
     * @param reqBo 用户信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.RealTimeHealthDataRspBo
     **************************************************************************************************************************/
    @Override
    public RealTimeHealthDataRspBo realTimeHealthData(RealTimeHealthDataReqBo reqBo) {
        log.info("Get user real-time health data request parameters, RealTimeHealthDataReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        RealTimeHealthDataRspBo healthDataRspBo = new RealTimeHealthDataRspBo();
        // 心率
        Page<HeartRate> page = heartRateService.page(
                new Page<HeartRate>(1, 1),
                new QueryWrapper<HeartRate>()
                        .eq("user_uuid", loginUser.getAccount())
                        .eq("family_member_id", reqBo.getFamilyMemberId())
                        .orderByDesc("create_time"));
        HeartRate heartRate = page.getRecords().get(0);
        if (heartRate != null) {
            healthDataRspBo.setSilentHeart(heartRate.getSilentHeart());
        }
        // 体温
        Page<Temperature> tempPage = temperatureService.page(
                new Page<Temperature>(1, 1),
                new QueryWrapper<Temperature>()
                        .eq("user_uuid", loginUser.getAccount())
                        .eq("family_member_id", reqBo.getFamilyMemberId())
                        .orderByDesc("create_time"));
        Temperature temperature = tempPage.getRecords().get(0);
        if (temperature != null) {
            healthDataRspBo.setTmpHandler(temperature.getTmpHandler());
            healthDataRspBo.setTmpForehead(temperature.getTmpForehead());
        }

        // 已过当天11点（以十一点为准）睡眠
        if (TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
            LocalDate localDate = LocalDateTime.now().toLocalDate();
            LocalDateTime toDayElevenClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0);
            LocalDateTime yesterdayNineClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth() - 1, HealthDataConstant.GRAB_SLEEP_TIME_TWENTY_ONE, 0, 0);
            int sleepCount = buildSleepTimeCounts(loginUser, reqBo, yesterdayNineClock, toDayElevenClock);
            healthDataRspBo.setSleepTime(sleepCount * HealthDataConstant.GRAB_SLEEP_TIME_STEP);
        }
        // 未过当天11点（以现在时间为准）睡眠
        if (!TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDate localDate = LocalDateTime.now().toLocalDate();
            LocalDateTime yesterdayNineClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth() - 1, HealthDataConstant.GRAB_SLEEP_TIME_TWENTY_ONE, 0, 0);
            int sleepCount = buildSleepTimeCounts(loginUser, reqBo, yesterdayNineClock, now);
            healthDataRspBo.setSleepTime(sleepCount * HealthDataConstant.GRAB_SLEEP_TIME_STEP);
        }
        return healthDataRspBo;
    }



    // --------------------------------------------------inner---------------------------------------------------------------

    /**
     * 获取用户进入睡眠状态的次数
     */
    private int buildSleepTimeCounts(@NotNull LoginUser loginUser,
                                     @NotNull RealTimeHealthDataReqBo reqBo,
                                     @NotNull LocalDateTime yesterdayNineClock,
                                     @NotNull LocalDateTime now) {
        int c = 0;
        List<Sleep> sleepList = sleepService.list(new LambdaQueryWrapper<Sleep>()
                .eq(Sleep::getUserUuid, loginUser.getAccount())
                .eq(Sleep::getFamilyMemberId, reqBo.getFamilyMemberId())
                .ge(Sleep::getCreateTime, yesterdayNineClock)
                .le(Sleep::getCreateTime, now));
        if (CollectionUtil.isNotEmpty(sleepList)) {
            for (Sleep sleep : sleepList) {
                if (HealthDataConstant.SLEEP_STATUS_TWO.equals(sleep.getSleepStatus()) || HealthDataConstant.SLEEP_STATUS_THREE.equals(sleep.getSleepStatus())) {
                    c++;
                }
            }
        }
        return c;
    }

}
