package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
import com.zhsj.community.yanglao_yiliao.healthydata.dto.SleepTitleTimeValueDto;
import com.zhsj.community.yanglao_yiliao.healthydata.dto.TempTitleTimeValueDto;
import com.zhsj.community.yanglao_yiliao.healthydata.dto.TimeValueDto;
import com.zhsj.community.yanglao_yiliao.healthydata.dto.TitleTimeValueDto;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        // HEART RATE
        Page<HeartRate> page = heartRateService.page(
                new Page<HeartRate>(1, 1),
                new QueryWrapper<HeartRate>()
                        .eq("user_uuid", loginUser.getAccount())
                        .eq("family_member_id", reqBo.getFamilyMemberId())
                        .eq("deleted", true)
                        .orderByDesc("create_time"));
        List<HeartRate> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            Integer silentHeart = records.get(0).getSilentHeart();
            healthDataRspBo.setSilentHeart(silentHeart);
            heartRateHealthStatus(healthDataRspBo, silentHeart);
        }
        // TEMP
        Page<Temperature> tempPage = temperatureService.page(
                new Page<Temperature>(1, 1),
                new QueryWrapper<Temperature>()
                        .eq("user_uuid", loginUser.getAccount())
                        .eq("family_member_id", reqBo.getFamilyMemberId())
                        .eq("deleted", true)
                        .orderByDesc("create_time"));
        List<Temperature> tempRecords = tempPage.getRecords();
        if (CollectionUtil.isNotEmpty(tempRecords)) {
            Double tmpHandler = tempRecords.get(0).getTmpHandler();
            healthDataRspBo.setTmpHandler(tmpHandler);
            tmpHandlerHealthStatus(healthDataRspBo, tmpHandler);
            Double tmpForehead = tempRecords.get(0).getTmpForehead();
            healthDataRspBo.setTmpForehead(tmpForehead);
            tmpForeheadHealthStatus(healthDataRspBo, tmpForehead);
        }
        // SLEEP 已过当天11点（以十一点为准）
        if (TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
            LocalDateTime toDayElevenClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0);
            LocalDateTime yesterdayNineClock = toDayElevenClock.plusHours(-14);
            int sleepCount = buildSleepTimeCounts(loginUser, reqBo, yesterdayNineClock, toDayElevenClock);
            if (sleepCount != 0) {
                int sleepTime = sleepCount * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
                healthDataRspBo.setSleepTime(sleepTime);
                sleepTimeHealthStatus(healthDataRspBo, sleepTime);
            }
        }
        // SLEEP 未过当天11点（以现在时间为准）
        if (!TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
            LocalDateTime yesterdayNineClock = (TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0)).plusHours(-3);
            int sleepCount = buildSleepTimeCounts(loginUser, reqBo, yesterdayNineClock, now);
            if (sleepCount != 0) {
                int sleepTime = sleepCount * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
                healthDataRspBo.setSleepTime(sleepTime);
                sleepTimeHealthStatus(healthDataRspBo, sleepTime);
            }
        }
        healthDataRspBo.setRefreshDataTime(TimeUtils.formatLocalDateTime(LocalDateTime.now()));
        buildTotalHealthStatus(healthDataRspBo);

        return healthDataRspBo;
    }

    /***************************************************************************************************************************
     * @description 查询用户心率图表信息
     * @author zzm
     * @date 2021/11/16 15:47
     * @param reqBo 用户信息和时间信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.HeartRateChartRspBo
     **************************************************************************************************************************/
    @Override
    public HeartRateChartRspBo heartRateChart(HeartRateChartReqBo reqBo) {
        log.info("Get heart rate chart request parameters, HeartRateChartReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        HeartRateChartRspBo rspBos = new HeartRateChartRspBo();
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        LocalDateTime todayZeroClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
        // --- By day
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_DAY.equals(reqBo.getTimeStatus())) {
            List<HeartRate> arr = heartRateService.list(new LambdaQueryWrapper<HeartRate>()
                    .eq(HeartRate::getUserUuid, loginUser.getAccount())
                    .eq(HeartRate::getFamilyMemberId, reqBo.getFamilyMemberId())
                    .ge(HeartRate::getCreateTime, todayZeroClock.plusHours(-6))
                    .le(HeartRate::getCreateTime, now)
                    .eq(HeartRate::getDeleted, true));
            int k = 0;
            int totalAvg = 0;
            List<TitleTimeValueDto> list = new ArrayList<>();
            for (int i = -6; i <= 24; i += 6) {
                LocalDateTime time1 = todayZeroClock.plusHours(i);
                LocalDateTime time2 = todayZeroClock.plusHours(i + 6);
                if (!arr.isEmpty()) {
                    int c1 = 0;
                    int j1 = 0;
                    int avg1 = 0;
                    for (HeartRate heartRate : arr) {
                        if (heartRate.getCreateTime().compareTo(time1) > 0 && heartRate.getCreateTime().compareTo(time2) < 0) {
                            c1 += heartRate.getSilentHeart();
                            j1++;
                        }
                    }
                    if (j1 != 0) {
                        avg1 = c1 / j1;
                        k += 1;
                        totalAvg += avg1;
                    }
                    if (now.compareTo(time2) > 0) {
                        list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(time2), TimeUtils.formatLocalDateTimeThird(time2), avg1));
                    } else {
                        list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(now), TimeUtils.formatLocalDateTimeThird(now), avg1));
                        break;
                    }
                } else {
                    if (now.compareTo(time2) > 0) {
                        list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(time2), TimeUtils.formatLocalDateTimeThird(time2), 0));
                    } else {
                        list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(now), TimeUtils.formatLocalDateTimeThird(now), 0));
                        break;
                    }
                }
            }
            rspBos.setList(list);
            if (k != 0) {
                buildHeartRateAvgAndStatus(totalAvg, k, rspBos);
            }
        }
        // --- By week
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_WEEK.equals(reqBo.getTimeStatus())) {
            buildHeartRateChartByDay(rspBos, todayZeroClock, loginUser, reqBo, 6);
        }
        // --- By month
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_MONTH.equals(reqBo.getTimeStatus())) {
            buildHeartRateChartByDay(rspBos, todayZeroClock, loginUser, reqBo, 29);
        }
        return rspBos;
    }

    /***************************************************************************************************************************
     * @description 获取用户心率异常记录列表
     * @author zzm
     * @date 2021/11/15 10:43
     * @param reqBo 用户id，查询时间类型
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String, java.lang.Integer>>
     **************************************************************************************************************************/
    @Override
    public List<AbnormalDataRspBo> abnormalHeartRateRecord(AbnormalDataReqBo reqBo) {
        log.info("Abnormal heart rate recording request parameters, AbnormalDataReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<AbnormalDataRspBo> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        LocalDateTime todayZeroClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
        // --- By day
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_DAY.equals(reqBo.getTimeStatus())) {
            List<HeartRate> rateList = heartRateList(loginUser, reqBo, todayZeroClock, now);
            if (CollectionUtil.isEmpty(rateList)) {
                return null;
            }
            List<TimeValueDto> arr = new ArrayList<>();
            for (HeartRate heartRate : rateList) {
                TimeValueDto timeValueDto = new TimeValueDto(TimeUtils.formatLocalDateTimeThird(heartRate.getCreateTime()), heartRate.getSilentHeart());
                arr.add(timeValueDto);
            }
            AbnormalDataRspBo abnormalDataRspBo = new AbnormalDataRspBo(TimeUtils.formatLocalDateTimeSecond(now), arr);
            list.add(abnormalDataRspBo);
        }
        // --- By week
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_WEEK.equals(reqBo.getTimeStatus())) {
            LocalDateTime sixDaysAgo = todayZeroClock.plusDays(-6);
            List<HeartRate> rateList = heartRateList(loginUser, reqBo, sixDaysAgo, now);
            if (CollectionUtil.isEmpty(rateList)) {
                return null;
            }
            String today = TimeUtils.formatLocalDateTimeSecond(now);
            queryAbnormalHeartRateFormat(rateList, today, list);
            for (int i = 1; i <= 6; i++) {
                String daysAge = TimeUtils.formatLocalDateTimeSecond(todayZeroClock.plusDays(-i));
                queryAbnormalHeartRateFormat(rateList, daysAge, list);
            }
        }
        // --- By month
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_MONTH.equals(reqBo.getTimeStatus())) {
            LocalDateTime twentyNineDaysAgo = todayZeroClock.plusDays(-29);
            List<HeartRate> rateList = heartRateList(loginUser, reqBo, twentyNineDaysAgo, now);
            if (CollectionUtil.isEmpty(rateList)) {
                return null;
            }
            String today = TimeUtils.formatLocalDateTimeSecond(now);
            queryAbnormalHeartRateFormat(rateList, today, list);
            for (int i = 1; i <= 29; i++) {
                String daysAge = TimeUtils.formatLocalDateTimeSecond(todayZeroClock.plusDays(-i));
                queryAbnormalHeartRateFormat(rateList, daysAge, list);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /***************************************************************************************************************************
     * @description 查询用户体温图表信息
     * @author zzm
     * @date 2021/11/17 9:57
     * @param reqBo 用户信息、时间信息
     * @return com.zhsj.community.yanglao_yiliao.healthydata.bo.TempChartRspBo
     **************************************************************************************************************************/
    @Override
    public TempChartRspBo tempChart(TempChartReqBo reqBo) {
        log.info("Get temp chart request parameters, TempChartReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        TempChartRspBo rspBos = new TempChartRspBo();
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        LocalDateTime todayZeroClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
        // --- By day
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_DAY.equals(reqBo.getTimeStatus())) {
            List<Temperature> temperatureList = temperatureService.list(new LambdaQueryWrapper<Temperature>()
                    .eq(Temperature::getUserUuid, loginUser.getAccount())
                    .eq(Temperature::getFamilyMemberId, reqBo.getFamilyMemberId())
                    .ge(Temperature::getCreateTime, todayZeroClock.plusHours(-6))
                    .le(Temperature::getCreateTime, now)
                    .eq(Temperature::getDeleted, true));
            int k = 0;
            double totalAvg = 0;
            List<TempTitleTimeValueDto> list = new ArrayList<>();
            for (int i = -6; i <= 24; i += 6) {
                LocalDateTime time1 = todayZeroClock.plusHours(i);
                LocalDateTime time2 = todayZeroClock.plusHours(i + 6);
                if (!temperatureList.isEmpty()) {
                    double c1 = 0;
                    int j1 = 0;
                    double avg1 = 0;
                    for (Temperature temperature : temperatureList) {
                        if (temperature.getCreateTime().compareTo(time1) > 0 && temperature.getCreateTime().compareTo(time2) < 0) {
                            c1 += temperature.getTmpHandler();
                            j1++;
                        }
                    }
                    if (j1 != 0) {
                        avg1 = c1 / j1;
                        k += 1;
                        totalAvg += avg1;
                    }
                    if (now.compareTo(time2) > 0) {
                        list.add(new TempTitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(time2), TimeUtils.formatLocalDateTimeThird(time2), String.format("%.1f", avg1)));
                    } else {
                        list.add(new TempTitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(now), TimeUtils.formatLocalDateTimeThird(now), String.format("%.1f", avg1)));
                        break;
                    }
                } else {
                    if (now.compareTo(time2) > 0) {
                        list.add(new TempTitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(time2), TimeUtils.formatLocalDateTimeThird(time2), "0.0"));
                    } else {
                        list.add(new TempTitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(now), TimeUtils.formatLocalDateTimeThird(now), "0.0"));
                        break;
                    }
                }
            }
            rspBos.setList(list);
            if (k != 0) {
                buildTempAvgAndStatus(totalAvg, k, rspBos);
            }
        }
        // --- By week
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_WEEK.equals(reqBo.getTimeStatus())) {
            buildTempChartByDay(rspBos, todayZeroClock, loginUser, reqBo, 6);
        }
        // --- By month
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_MONTH.equals(reqBo.getTimeStatus())) {
            buildTempChartByDay(rspBos, todayZeroClock, loginUser, reqBo, 29);
        }
        return rspBos;
    }

    /***************************************************************************************************************************
     * @description 根据时间类型获取用户体温异常记录
     * @author zzm
     * @date 2021/11/16 17:04
     * @param reqBo 用户id，查询时间类型
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.AbnormalDataRspBo>
     **************************************************************************************************************************/
    @Override
    public List<AbnormalDataRspBo> abnormalTempRecord(AbnormalDataReqBo reqBo) {
        log.info("Abnormal temperature recording request parameters, AbnormalDataReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<AbnormalDataRspBo> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        LocalDateTime todayZeroClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
        // --- By day
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_DAY.equals(reqBo.getTimeStatus())) {
            List<Temperature> temperatureList = temperatureList(loginUser, reqBo, todayZeroClock, now);
            if (CollectionUtil.isEmpty(temperatureList)) {
                return null;
            }
            List<TimeValueDto> arr = new ArrayList<>();
            for (Temperature temperature : temperatureList) {
                TimeValueDto timeValueDto = new TimeValueDto(TimeUtils.formatLocalDateTimeThird(temperature.getCreateTime()), temperature.getTmpHandler());
                arr.add(timeValueDto);
            }
            AbnormalDataRspBo abnormalDataRspBo = new AbnormalDataRspBo(TimeUtils.formatLocalDateTimeSecond(now), arr);
            list.add(abnormalDataRspBo);
        }
        // --- By week
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_WEEK.equals(reqBo.getTimeStatus())) {
            LocalDateTime sixDaysAgo = todayZeroClock.plusDays(-6);
            List<Temperature> temperatureList = temperatureList(loginUser, reqBo, sixDaysAgo, now);
            if (CollectionUtil.isEmpty(temperatureList)) {
                return null;
            }
            String today = TimeUtils.formatLocalDateTimeSecond(now);
            queryAbnormalTemperatureFormat(temperatureList, today, list);
            for (int i = 1; i <= 6; i++) {
                String daysAge = TimeUtils.formatLocalDateTimeSecond(todayZeroClock.plusDays(-i));
                queryAbnormalTemperatureFormat(temperatureList, daysAge, list);
            }
        }
        // --- By month
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_MONTH.equals(reqBo.getTimeStatus())) {
            LocalDateTime twentyNineDaysAgo = todayZeroClock.plusDays(-29);
            List<Temperature> temperatureList = temperatureList(loginUser, reqBo, twentyNineDaysAgo, now);
            if (CollectionUtil.isEmpty(temperatureList)) {
                return null;
            }
            String today = TimeUtils.formatLocalDateTimeSecond(now);
            queryAbnormalTemperatureFormat(temperatureList, today, list);
            for (int i = 1; i <= 29; i++) {
                String daysAge = TimeUtils.formatLocalDateTimeSecond(todayZeroClock.plusDays(-i));
                queryAbnormalTemperatureFormat(temperatureList, daysAge, list);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /***************************************************************************************************************************
     * @description 根据用户按时间类型查询用户睡眠图表信息
     * @author zzm
     * @date 2021/11/17 15:30
     * @param reqBo 用户信息、时间信息
     * @return java.util.List<com.zhsj.community.yanglao_yiliao.healthydata.bo.SleepChartRspBo>
     **************************************************************************************************************************/
    @Override
    public SleepChartRspBo sleepChart(SleepChartReqBo reqBo) {
        log.info("Query user sleep chart information request parameters, SleepChartReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        SleepChartRspBo sleepChartRspBo = new SleepChartRspBo();
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        LocalDate nowLocalDate = nowLocalDateTime.toLocalDate();
        // --- BY DAY
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_DAY.equals(reqBo.getTimeStatus())) {
            // 已过当天11点（以十一点为准）
            if (TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
                LocalDateTime toDayElevenClock = TimeUtils.buildLocalDateTime(nowLocalDate.getYear(), nowLocalDate.getMonthValue(), nowLocalDate.getDayOfMonth(), HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0);
                LocalDateTime yesterdayNineClock = toDayElevenClock.plusHours(-14);
                byDayBuildSleepChart(loginUser, reqBo, sleepChartRspBo, yesterdayNineClock, toDayElevenClock);
            }
            // 未过当天11点（以现在时间为准）
            if (!TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
                LocalDateTime yesterdayNineClock = (TimeUtils.buildLocalDateTime(nowLocalDate.getYear(), nowLocalDate.getMonthValue(), nowLocalDate.getDayOfMonth(), 0, 0, 0)).plusHours(-3);
                byDayBuildSleepChart(loginUser, reqBo, sleepChartRspBo, yesterdayNineClock, nowLocalDateTime);
            }
        }
        // ---BY WEEK
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_WEEK.equals(reqBo.getTimeStatus())) {
            LocalDateTime toDayElevenClock = TimeUtils.buildLocalDateTime(nowLocalDate.getYear(), nowLocalDate.getMonthValue(), nowLocalDate.getDayOfMonth(), HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0);
            LocalDateTime yesterdayNineClock = toDayElevenClock.plusHours(-14);
            int sevenDayTotalSleepTime = 0;
            List<SleepTitleTimeValueDto> arr = new ArrayList<>();
            // -1(-6,0) -2(-12-1,-6-1) -3(-18-2,-12-2)
            int start = reqBo.getPageTurnStatus() * 6 + (reqBo.getPageTurnStatus() + 1);
            int end = reqBo.getPageTurnStatus() * 6 + 6 + (reqBo.getPageTurnStatus() + 1);
            LocalDateTime time1 = yesterdayNineClock.plusDays(start);
            LocalDateTime time2 = toDayElevenClock.plusDays(end);
            List<Sleep> list = selectSleepChartData(loginUser, reqBo, time1, time2);
            for (int i = start; i <= end; i++) {
                LocalDateTime time3 = yesterdayNineClock.plusDays(i);
                LocalDateTime time4 = toDayElevenClock.plusDays(i);
                ArrayList<Sleep> sleepList = new ArrayList<>();
                for (Sleep sleep : list) {
                    if (sleep.getCreateTime().compareTo(time3) >= 0 && sleep.getCreateTime().compareTo(time4) <= 0) {
                        sleepList.add(sleep);
                    }
                }
                SleepTitleTimeValueDto sleepTitleTimeValueDto = new SleepTitleTimeValueDto();
                sleepTitleTimeValueDto.setTimeTitle(TimeUtils.formatLocalDateTimeFifth(time3)).setTimeValue(TimeUtils.formatLocalDateTimeSixth(time3)).setTimeWeek(buildWeek(time3));
                if (CollectionUtil.isEmpty(sleepList)) {
                    sleepTitleTimeValueDto.setDeepSleepTime(0).setLightSleepTime(0).setWakeUpTime(0).setSleepScore("0").setTotalSleepTime(0);
                    arr.add(sleepTitleTimeValueDto);
                    continue;
                }
                Integer c = commonBuildSleepChart(sleepList, sleepTitleTimeValueDto, arr);
                sevenDayTotalSleepTime += c;
            }

            LocalDateTime time5 = yesterdayNineClock.plusDays((reqBo.getPageTurnStatus() - 1) * 6 + reqBo.getPageTurnStatus());
            LocalDateTime time6 = toDayElevenClock.plusDays((reqBo.getPageTurnStatus() - 1) * 6 + 6 + reqBo.getPageTurnStatus());
            List<Sleep> sleepList = selectSleepChartData(loginUser, reqBo, time5, time6);
            if (CollectionUtil.isEmpty(sleepList)) {
                sleepChartRspBo.setCompareAvgSleepTime(sevenDayTotalSleepTime / 7);
            } else {
                int v = 0;
                for (Sleep sleep : sleepList) {
                    if (HealthDataConstant.SLEEP_STATUS_TWO.equals(sleep.getSleepStatus()) || HealthDataConstant.SLEEP_STATUS_THREE.equals(sleep.getSleepStatus())) {
                        v++;
                    }
                }
                sleepChartRspBo.setCompareAvgSleepTime(sevenDayTotalSleepTime / 7 - v * HealthDataConstant.GRAB_SLEEP_TIME_STEP / 7);
            }

            sleepChartRspBo.setList(arr).setLastSevenDayTotalSleepTime(sevenDayTotalSleepTime).setLastSevenDayAvgSleepTime(sevenDayTotalSleepTime / 7);
        }
        return sleepChartRspBo;
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
                .le(Sleep::getCreateTime, now)
                .eq(Sleep::getDeleted, true));
        if (CollectionUtil.isNotEmpty(sleepList)) {
            for (Sleep sleep : sleepList) {
                if (HealthDataConstant.SLEEP_STATUS_TWO.equals(sleep.getSleepStatus()) || HealthDataConstant.SLEEP_STATUS_THREE.equals(sleep.getSleepStatus())) {
                    c++;
                }
            }
        }
        return c;
    }

    /**
     * 设值心率健康状态
     */
    private void heartRateHealthStatus(@NotNull RealTimeHealthDataRspBo healthDataRspBo,
                                       @NotNull Integer silentHeart) {
        if (silentHeart >= 60 && silentHeart <= 100) {
            healthDataRspBo.setSilentHeartHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_GREEN);
        }
        if (silentHeart >= 101 && silentHeart <= 160 || silentHeart >= 40 && silentHeart <= 59) {
            healthDataRspBo.setSilentHeartHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW);
        }
        if (silentHeart < 40 || silentHeart > 160) {
            healthDataRspBo.setSilentHeartHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_RED);
        }
    }

    /**
     * 设值手腕体温健康状态
     */
    private void tmpHandlerHealthStatus(@NotNull RealTimeHealthDataRspBo healthDataRspBo,
                                        @NotNull Double tmpHandler) {
        if (tmpHandler >= 36 && tmpHandler <= 37) {
            healthDataRspBo.setTmpHandlerHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_GREEN);
        }
        if (tmpHandler > 37 && tmpHandler <= 38 || tmpHandler >= 35 && tmpHandler < 36) {
            healthDataRspBo.setTmpHandlerHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW);
        }
        if (tmpHandler < 35 || tmpHandler > 38) {
            healthDataRspBo.setTmpHandlerHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_RED);
        }
    }

    /**
     * 设值额头体温健康状态
     */
    private void tmpForeheadHealthStatus(@NotNull RealTimeHealthDataRspBo healthDataRspBo,
                                         @NotNull Double tmpForehead) {
        if (tmpForehead >= 36 && tmpForehead <= 37) {
            healthDataRspBo.setTmpForeheadHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_GREEN);
        }
        if (tmpForehead > 37 && tmpForehead <= 38 || tmpForehead >= 35 && tmpForehead < 36) {
            healthDataRspBo.setTmpForeheadHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW);
        }
        if (tmpForehead < 35 || tmpForehead > 38) {
            healthDataRspBo.setTmpForeheadHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_RED);
        }
    }

    /**
     * 设值睡眠时间健康状态
     */
    private void sleepTimeHealthStatus(@NotNull RealTimeHealthDataRspBo healthDataRspBo,
                                       @NotNull Integer sleepTime) {
        if (sleepTime >= 360) {
            healthDataRspBo.setSleepHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_GREEN);
        }
        if (sleepTime >= 240 && sleepTime < 360) {
            healthDataRspBo.setSleepHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW);
        }
        if (sleepTime < 240) {
            healthDataRspBo.setSleepHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_RED);
        }
    }

    /**
     * 构建用户总的身体健康状态
     */
    private void buildTotalHealthStatus(@NotNull RealTimeHealthDataRspBo healthDataRspBo) {
        int score = 10;
        if (healthDataRspBo.getSilentHeartHealthStatus() != null) {
            if (HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW.equals(healthDataRspBo.getSilentHeartHealthStatus())) {
                score -= 2;
            }
            if (HealthDataConstant.HEALTH_COLOR_STATUS_RED.equals(healthDataRspBo.getSilentHeartHealthStatus())) {
                score -= 3;
            }
        }
        if (healthDataRspBo.getTmpHandlerHealthStatus() != null) {
            if (HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW.equals(healthDataRspBo.getTmpHandlerHealthStatus())) {
                score -= 2;
            }
            if (HealthDataConstant.HEALTH_COLOR_STATUS_RED.equals(healthDataRspBo.getTmpHandlerHealthStatus())) {
                score -= 3;
            }
        }
        if (healthDataRspBo.getSleepHealthStatus() != null) {
            if (HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW.equals(healthDataRspBo.getSleepHealthStatus())) {
                score -= 2;
            }
            if (HealthDataConstant.HEALTH_COLOR_STATUS_RED.equals(healthDataRspBo.getSleepHealthStatus())) {
                score -= 3;
            }
        }
        if (score >= 8 && score <= 10) {
            healthDataRspBo.setUserTotalHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_GREEN);
        }
        if (score >= 5 && score <= 7) {
            healthDataRspBo.setUserTotalHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW);
        }
        if (score >= 1 && score <= 4) {
            healthDataRspBo.setUserTotalHealthStatus(HealthDataConstant.HEALTH_COLOR_STATUS_YELLOW);
        }
    }

    /**
     * 根据条件查询用户心率异常列表
     */
    private List<HeartRate> heartRateList(@NotNull LoginUser loginUser,
                                          @NotNull AbnormalDataReqBo reqBo,
                                          @NotNull LocalDateTime zeroClock,
                                          @NotNull LocalDateTime now) {
        return heartRateService.list(new LambdaQueryWrapper<HeartRate>()
                .eq(HeartRate::getUserUuid, loginUser.getAccount())
                .eq(HeartRate::getFamilyMemberId, reqBo.getFamilyMemberId())
                .ge(HeartRate::getCreateTime, zeroClock)
                .le(HeartRate::getCreateTime, now)
                .notBetween(HeartRate::getSilentHeart, 60, 100)
                .eq(HeartRate::getDeleted, true)
                .orderByAsc(HeartRate::getCreateTime));
    }

    /**
     * 根据条件查询用户体温异常列表
     */
    private List<Temperature> temperatureList(@NotNull LoginUser loginUser,
                                              @NotNull AbnormalDataReqBo reqBo,
                                              @NotNull LocalDateTime zeroClock,
                                              @NotNull LocalDateTime now) {
        return temperatureService.list(new LambdaQueryWrapper<Temperature>()
                .eq(Temperature::getUserUuid, loginUser.getAccount())
                .eq(Temperature::getFamilyMemberId, reqBo.getFamilyMemberId())
                .ge(Temperature::getCreateTime, zeroClock)
                .le(Temperature::getCreateTime, now)
                .notBetween(Temperature::getTmpHandler, 36, 37)
                .eq(Temperature::getDeleted, true)
                .orderByAsc(Temperature::getCreateTime));
    }

    /**
     * 按周或月查心率异常记录列表格式化返回数据
     */
    private void queryAbnormalHeartRateFormat(@NotNull List<HeartRate> rateList,
                                              @NotNull String timeFormat,
                                              @NotNull List<AbnormalDataRspBo> list) {
        List<TimeValueDto> dtoList = new ArrayList<>();
        for (HeartRate heartRate : rateList) {
            if (TimeUtils.formatLocalDateTimeSecond(heartRate.getCreateTime()).equals(timeFormat)) {
                TimeValueDto timeValueDto = new TimeValueDto(TimeUtils.formatLocalDateTimeThird(heartRate.getCreateTime()), heartRate.getSilentHeart());
                dtoList.add(timeValueDto);
            }
        }
        if (!dtoList.isEmpty()) {
            AbnormalDataRspBo abnormalDataRspBo = new AbnormalDataRspBo(timeFormat, dtoList);
            list.add(abnormalDataRspBo);
        }
    }

    /**
     * 按周或月查体温异常记录列表格式化返回数据
     */
    private void queryAbnormalTemperatureFormat(@NotNull List<Temperature> temperatureList,
                                                @NotNull String timeFormat,
                                                @NotNull List<AbnormalDataRspBo> list) {
        List<TimeValueDto> dtoList = new ArrayList<>();
        for (Temperature temperature : temperatureList) {
            if (TimeUtils.formatLocalDateTimeSecond(temperature.getCreateTime()).equals(timeFormat)) {
                TimeValueDto timeValueDto = new TimeValueDto(TimeUtils.formatLocalDateTimeThird(temperature.getCreateTime()), temperature.getTmpHandler());
                dtoList.add(timeValueDto);
            }
        }
        if (!dtoList.isEmpty()) {
            AbnormalDataRspBo abnormalDataRspBo = new AbnormalDataRspBo(timeFormat, dtoList);
            list.add(abnormalDataRspBo);
        }
    }

    /**
     * 通过以天为单位构建心率图表数据（过去7天，过去30天）
     */
    private void buildHeartRateChartByDay(@NotNull HeartRateChartRspBo rspBos,
                                          @NotNull LocalDateTime todayZeroClock,
                                          @NotNull LoginUser loginUser,
                                          @NotNull HeartRateChartReqBo reqBo,
                                          @NotNull Integer num
    ) {
        List<HeartRate> rateList = heartRateService.list(new LambdaQueryWrapper<HeartRate>()
                .eq(HeartRate::getUserUuid, loginUser.getAccount())
                .eq(HeartRate::getFamilyMemberId, reqBo.getFamilyMemberId())
                .ge(HeartRate::getCreateTime, todayZeroClock.plusDays(-num))
                .le(HeartRate::getCreateTime, todayZeroClock.plusDays(1))
                .eq(HeartRate::getDeleted, true));
        int k = 0;
        int totalAvg = 0;
        List<TitleTimeValueDto> list = new ArrayList<>();
        for (int i = -num; i <= 0; i++) {
            LocalDateTime time1 = todayZeroClock.plusDays(i);
            LocalDateTime time2 = todayZeroClock.plusDays(i + 1);
            if (!rateList.isEmpty()) {
                int c1 = 0;
                int j1 = 0;
                int avg1 = 0;
                for (HeartRate heartRate : rateList) {
                    if (heartRate.getCreateTime().compareTo(time1) >= 0 && heartRate.getCreateTime().compareTo(time2) <= 0) {
                        c1 += heartRate.getSilentHeart();
                        j1++;
                    }
                }
                if (j1 != 0) {
                    avg1 = c1 / j1;
                    k += 1;
                    totalAvg += avg1;
                }
                list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFifth(time1), TimeUtils.formatLocalDateTimeSixth(time1), avg1));
            } else {
                list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFifth(time1), TimeUtils.formatLocalDateTimeSixth(time1), 0));
            }
        }
        rspBos.setList(list);
        if (k != 0) {
            buildHeartRateAvgAndStatus(totalAvg, k, rspBos);
        }
    }

    /**
     * 构建用户心率平均值和健康状态并设值返回
     */
    private void buildHeartRateAvgAndStatus(@NotNull Integer totalAvg,
                                            @NotNull Integer k,
                                            @NotNull HeartRateChartRspBo rspBos) {
        int dayHeartRateAvg = totalAvg / k;
        rspBos.setSilentHeartAvg(dayHeartRateAvg);
        if (dayHeartRateAvg >= 60 && dayHeartRateAvg <= 100) {
            rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_NORMAL);
        }
        if (dayHeartRateAvg < 60) {
            rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_LOWER);
        }
        if (dayHeartRateAvg > 100) {
            rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_HIGHER);
        }
    }

    /**
     * 通过以天为单位构建体温图表数据（过去7天，过去30天）
     */
    private void buildTempChartByDay(@NotNull TempChartRspBo rspBos,
                                     @NotNull LocalDateTime todayZeroClock,
                                     @NotNull LoginUser loginUser,
                                     @NotNull TempChartReqBo reqBo,
                                     @NotNull Integer num
    ) {
        List<Temperature> tempList = temperatureService.list(new LambdaQueryWrapper<Temperature>()
                .eq(Temperature::getUserUuid, loginUser.getAccount())
                .eq(Temperature::getFamilyMemberId, reqBo.getFamilyMemberId())
                .ge(Temperature::getCreateTime, todayZeroClock.plusDays(-num))
                .le(Temperature::getCreateTime, todayZeroClock.plusDays(1))
                .eq(Temperature::getDeleted, true)
                .orderByAsc(Temperature::getCreateTime));
        int k = 0;
        double totalAvg = 0;
        List<TempTitleTimeValueDto> list = new ArrayList<>();
        for (int i = -num; i <= 0; i++) {
            LocalDateTime time1 = todayZeroClock.plusDays(i);
            LocalDateTime time2 = todayZeroClock.plusDays(i + 1);
            if (!tempList.isEmpty()) {
                double c1 = 0;
                int j1 = 0;
                double avg1 = 0;
                for (Temperature temperature : tempList) {
                    if (temperature.getCreateTime().compareTo(time1) > 0 && temperature.getCreateTime().compareTo(time2) < 0) {
                        c1 += temperature.getTmpHandler();
                        j1++;
                    }
                }
                if (j1 != 0) {
                    avg1 = c1 / j1;
                    k += 1;
                    totalAvg += avg1;
                }
                list.add(new TempTitleTimeValueDto(TimeUtils.formatLocalDateTimeFifth(time1), TimeUtils.formatLocalDateTimeSixth(time1), String.format("%.1f", avg1)));
            } else {
                list.add(new TempTitleTimeValueDto(TimeUtils.formatLocalDateTimeFifth(time1), TimeUtils.formatLocalDateTimeSixth(time1), "0.0"));
            }
        }
        rspBos.setList(list);
        if (k != 0) {
            buildTempAvgAndStatus(totalAvg, k, rspBos);
        }
    }

    /**
     * 构建用户体温平均值和健康状态并设值返回
     */
    private void buildTempAvgAndStatus(@NotNull Double totalAvg,
                                       @NotNull Integer k,
                                       @NotNull TempChartRspBo rspBos) {
        double dayHeartRateAvg = totalAvg / k;
        rspBos.setTemptAvg(String.format("%.1f", dayHeartRateAvg));
        if (dayHeartRateAvg >= 36 && dayHeartRateAvg <= 37) {
            rspBos.setTempStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_NORMAL);
        }
        if (dayHeartRateAvg < 36) {
            rspBos.setTempStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_LOWER);
        }
        if (dayHeartRateAvg > 37) {
            rspBos.setTempStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_HIGHER);
        }
    }

    /**
     * 查询用户睡眠数据
     */
    private List<Sleep> selectSleepChartData(LoginUser loginUser, SleepChartReqBo reqBo, LocalDateTime time1, LocalDateTime time2) {
        return sleepService.list(new LambdaQueryWrapper<Sleep>()
                .eq(Sleep::getUserUuid, loginUser.getAccount())
                .eq(Sleep::getFamilyMemberId, reqBo.getFamilyMemberId())
                .ge(Sleep::getCreateTime, time1)
                .le(Sleep::getCreateTime, time2)
                .eq(Sleep::getDeleted, true));
    }

    /**
     * 用户睡眠图表信息公共构建部分
     */
    private Integer commonBuildSleepChart(@NotNull List<Sleep> sleepList,
                                          @NotNull SleepTitleTimeValueDto sleepTitleTimeValueDto,
                                          @NotNull List<SleepTitleTimeValueDto> arr) {
        int sevenDayTotalSleepTime = 0;
        int light = 0;
        int deep = 0;
        int wakeUp = 0;
        for (Sleep sleep : sleepList) {
            if (HealthDataConstant.SLEEP_STATUS_TWO.equals(sleep.getSleepStatus())) {
                light++;
            }
            if (HealthDataConstant.SLEEP_STATUS_THREE.equals(sleep.getSleepStatus())) {
                deep++;
            }
            if (HealthDataConstant.SLEEP_STATUS_FOUR.equals(sleep.getSleepStatus())) {
                wakeUp++;
            }
        }
        int lightSleepTime = light * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
        int deepSleepTime = deep * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
        int wakeUpSleepTime = wakeUp * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
        sevenDayTotalSleepTime += lightSleepTime;
        sevenDayTotalSleepTime += deepSleepTime;
        double sleepScore = (deepSleepTime / (2.5 * 60)) * 0.4 + (lightSleepTime / (5.5 * 60)) * 0.6;
        DecimalFormat df = new DecimalFormat("#");
        String formatSleepScore = df.format(sleepScore * 100);
        sleepTitleTimeValueDto.setDeepSleepTime(deepSleepTime).setLightSleepTime(lightSleepTime).setWakeUpTime(wakeUpSleepTime).setSleepScore(formatSleepScore).setTotalSleepTime(lightSleepTime + deepSleepTime);
        arr.add(sleepTitleTimeValueDto);
        return sevenDayTotalSleepTime;
    }

    /**
     * 通过天构建用户睡眠图表信息
     */
    private void byDayBuildSleepChart(
            @NotNull LoginUser loginUser,
            @NotNull SleepChartReqBo reqBo,
            @NotNull SleepChartRspBo sleepChartRspBo,
            @NotNull LocalDateTime yesterdayNineClock,
            @NotNull LocalDateTime toDayClock) {
        List<SleepTitleTimeValueDto> arr = new ArrayList<>();
        SleepTitleTimeValueDto sleepTitleTimeValueDto = new SleepTitleTimeValueDto();
        sleepTitleTimeValueDto.setTimeTitle(TimeUtils.formatLocalDateTimeFifth(yesterdayNineClock))
                .setTimeValue(TimeUtils.formatLocalDateTimeSixth(yesterdayNineClock))
                .setTimeWeek(buildWeek(yesterdayNineClock));

        List<Sleep> sleepList = selectSleepChartData(loginUser, reqBo, yesterdayNineClock, toDayClock);
        if (CollectionUtil.isEmpty(sleepList)) {
            sleepTitleTimeValueDto.setDeepSleepTime(0).setLightSleepTime(0).setWakeUpTime(0).setSleepScore("0").setTotalSleepTime(0);
            arr.add(sleepTitleTimeValueDto);
            sleepChartRspBo.setList(arr);
            return;
        }
        commonBuildSleepChart(sleepList, sleepTitleTimeValueDto, arr);
        sleepChartRspBo.setList(arr);
    }

    private String buildWeek(@NotNull LocalDateTime localDateTime) {
        int weekValue = localDateTime.getDayOfWeek().getValue();
        if (weekValue == 1) {
            return "周一";
        } else if (weekValue == 2) {
            return "周二";
        } else if (weekValue == 3) {
            return "周三";
        } else if (weekValue == 4) {
            return "周四";
        } else if (weekValue == 5) {
            return "周五";
        } else if (weekValue == 6) {
            return "周六";
        } else {
            return "周日";
        }
    }
}
