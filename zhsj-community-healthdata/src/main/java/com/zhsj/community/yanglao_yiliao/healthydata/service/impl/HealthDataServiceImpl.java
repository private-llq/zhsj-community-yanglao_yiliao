package com.zhsj.community.yanglao_yiliao.healthydata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.*;
import com.zhsj.community.yanglao_yiliao.healthydata.constant.HealthDataConstant;
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

        // 心率
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

        // 体温
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

        // 已过当天11点（以十一点为准）睡眠
        if (TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
            LocalDate localDate = LocalDateTime.now().toLocalDate();
            LocalDateTime toDayElevenClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0);
            LocalDateTime yesterdayNineClock = toDayElevenClock.plusHours(-14);
            int sleepCount = buildSleepTimeCounts(loginUser, reqBo, yesterdayNineClock, toDayElevenClock);
            int sleepTime = sleepCount * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
            healthDataRspBo.setSleepTime(sleepTime);
            sleepTimeHealthStatus(healthDataRspBo, sleepTime);
        }
        // 未过当天11点（以现在时间为准）睡眠
        if (!TimeUtils.isBefore(HealthDataConstant.GRAB_SLEEP_TIME_ELEVEN, 0, 0)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDate localDate = LocalDateTime.now().toLocalDate();
            LocalDateTime yesterdayNineClock = (TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0)).plusHours(-3);
            int sleepCount = buildSleepTimeCounts(loginUser, reqBo, yesterdayNineClock, now);
            int sleepTime = sleepCount * HealthDataConstant.GRAB_SLEEP_TIME_STEP;
            healthDataRspBo.setSleepTime(sleepTime);
            sleepTimeHealthStatus(healthDataRspBo, sleepTime);
        }
        healthDataRspBo.setRefreshDataTime(TimeUtils.formatLocalDateTime(LocalDateTime.now()));
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
            Map<Integer, List<HeartRate>> map = new HashMap<>();
            for (int i = -6; i <= 24; i += 6) {
                List<HeartRate> rateList = heartRateService.list(new LambdaQueryWrapper<HeartRate>()
                        .eq(HeartRate::getUserUuid, loginUser.getAccount())
                        .eq(HeartRate::getFamilyMemberId, reqBo.getFamilyMemberId())
                        .ge(HeartRate::getCreateTime, todayZeroClock.plusHours(i))
                        .le(HeartRate::getCreateTime, todayZeroClock.plusHours(i + 6))
                        .eq(HeartRate::getDeleted, true)
                        .orderByAsc(HeartRate::getCreateTime));
                map.put(i + 6, rateList);
            }
            int k = 0;
            int totalAvg = 0;
            List<TitleTimeValueDto> list = new ArrayList<>();
            for (int i = 0; i <= 24; i += 6) {
                List<HeartRate> rateList = map.get(i);
                if (!rateList.isEmpty()) {
                    int c1 = 0;
                    int avg1 = 0;
                    for (HeartRate heartRate : rateList) {
                        c1 += heartRate.getSilentHeart();
                    }
                    avg1 = c1 / rateList.size();
                    list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(todayZeroClock.plusHours(i)), TimeUtils.formatLocalDateTimeThird(todayZeroClock.plusHours(i)), avg1));

                    k += 1;
                    totalAvg += avg1;
                } else {
                    list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFourth(todayZeroClock.plusHours(i)), TimeUtils.formatLocalDateTimeThird(todayZeroClock.plusHours(i)), 0));
                }
            }
            rspBos.setList(list);
            if (k != 0) {
                int dayHeartRateAvg = totalAvg / k;
                rspBos.setSilentHeartAvg(dayHeartRateAvg);
                if (dayHeartRateAvg >= 60 && dayHeartRateAvg <= 100) {
                    rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_NORMAL);
                }
                if (dayHeartRateAvg >= 40 && dayHeartRateAvg <= 59) {
                    rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_LOW);
                }
                if (dayHeartRateAvg >= 101 && dayHeartRateAvg <= 160) {
                    rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_HIGH);
                }
                if (dayHeartRateAvg < 40) {
                    rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_LOWER);
                }
                if (dayHeartRateAvg > 160) {
                    rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_HIGHER);
                }
            } else {
                rspBos.setSilentHeartAvg(0);
                rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_STATUS_NOT_HAVE_AVG);
            }
        }
        // --- By week
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_WEEK.equals(reqBo.getTimeStatus())) {
            buildHeartRateChartByDay(rspBos,todayZeroClock,loginUser,reqBo,6);
        }
        // --- By month
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_MONTH.equals(reqBo.getTimeStatus())) {
            buildHeartRateChartByDay(rspBos,todayZeroClock,loginUser,reqBo,29);
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
    public List<AbnormalDataRspBo> abnormalHeartRateRecord(AbnormalHeartRateRecordReqBo reqBo) {
        log.info("Abnormal heart rate recording request parameters, AbnormalHeartRateRecordReqBo = {}", reqBo);
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        List<AbnormalDataRspBo> list = new ArrayList<>();
        // --- By day
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_DAY.equals(reqBo.getTimeStatus())) {
            LocalDateTime now = LocalDateTime.now();
            LocalDate localDate = now.toLocalDate();
            LocalDateTime zeroClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
            List<HeartRate> rateList = heartRateList(loginUser, reqBo, zeroClock, now);
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
            LocalDateTime now = LocalDateTime.now();
            LocalDate localDate = now.toLocalDate();
            LocalDateTime localDateTime = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
            LocalDateTime sixDaysAgo = localDateTime.plusDays(-6);
            List<HeartRate> rateList = heartRateList(loginUser, reqBo, sixDaysAgo, now);
            if (CollectionUtil.isEmpty(rateList)) {
                return null;
            }
            String today = TimeUtils.formatLocalDateTimeSecond(now);
            queryAbnormalHeartRateFormat(rateList, today, list);
            for (int i = 1; i <= 6; i++) {
                String daysAge = TimeUtils.formatLocalDateTimeSecond(localDateTime.plusDays(-i));
                queryAbnormalHeartRateFormat(rateList, daysAge, list);
            }
        }
        // --- By month
        if (HealthDataConstant.HEALTH_DATA_SELECT_CHART_TIME_MONTH.equals(reqBo.getTimeStatus())) {
            LocalDateTime now = LocalDateTime.now();
            LocalDate localDate = now.toLocalDate();
            LocalDateTime localDateTime = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
            LocalDateTime twentyNineDaysAgo = localDateTime.plusDays(-29);
            List<HeartRate> rateList = heartRateList(loginUser, reqBo, twentyNineDaysAgo, now);
            if (CollectionUtil.isEmpty(rateList)) {
                return null;
            }
            String today = TimeUtils.formatLocalDateTimeSecond(now);
            queryAbnormalHeartRateFormat(rateList, today, list);
            for (int i = 1; i <= 29; i++) {
                String daysAge = TimeUtils.formatLocalDateTimeSecond(localDateTime.plusDays(-i));
                queryAbnormalHeartRateFormat(rateList, daysAge, list);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
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
     * 根据条件查询用户心率异常列表
     */
    private List<HeartRate> heartRateList(LoginUser loginUser,
                                          AbnormalHeartRateRecordReqBo reqBo,
                                          LocalDateTime zeroClock,
                                          LocalDateTime now) {
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
     * 通过以天为单位构建心率图表数据（过去7天，过去30天）
     */
    private void buildHeartRateChartByDay(@NotNull HeartRateChartRspBo rspBos,
                                          @NotNull LocalDateTime todayZeroClock,
                                          @NotNull LoginUser loginUser,
                                          @NotNull HeartRateChartReqBo reqBo,
                                          @NotNull Integer num
    ) {
        Map<Integer, List<HeartRate>> map = new HashMap<>();
        for (int i = -num; i <= 0; i++) {
            List<HeartRate> rateList = heartRateService.list(new LambdaQueryWrapper<HeartRate>()
                    .eq(HeartRate::getUserUuid, loginUser.getAccount())
                    .eq(HeartRate::getFamilyMemberId, reqBo.getFamilyMemberId())
                    .ge(HeartRate::getCreateTime, todayZeroClock.plusDays(i))
                    .le(HeartRate::getCreateTime, todayZeroClock.plusDays(i + 1))
                    .eq(HeartRate::getDeleted, true)
                    .orderByAsc(HeartRate::getCreateTime));
            map.put(i, rateList);
        }
        int k = 0;
        int totalAvg = 0;
        List<TitleTimeValueDto> list = new ArrayList<>();
        for (int i = -num; i <= 0; i++) {
            List<HeartRate> rateList = map.get(i);
            if (!rateList.isEmpty()) {
                int c1 = 0;
                int avg1;
                for (HeartRate heartRate : rateList) {
                    c1 += heartRate.getSilentHeart();
                }
                avg1 = c1 / rateList.size();
                list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFifth(todayZeroClock.plusDays(i)), TimeUtils.formatLocalDateTimeSixth(todayZeroClock.plusDays(i)), avg1));

                k += 1;
                totalAvg += avg1;
            } else {
                list.add(new TitleTimeValueDto(TimeUtils.formatLocalDateTimeFifth(todayZeroClock.plusDays(i)), TimeUtils.formatLocalDateTimeSixth(todayZeroClock.plusDays(i)), 0));
            }
        }
        rspBos.setList(list);
        if (k != 0) {
            int dayHeartRateAvg = totalAvg / k;
            rspBos.setSilentHeartAvg(dayHeartRateAvg);
            if (dayHeartRateAvg >= 60 && dayHeartRateAvg <= 100) {
                rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_NORMAL);
            }
            if (dayHeartRateAvg >= 40 && dayHeartRateAvg <= 59) {
                rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_LOW);
            }
            if (dayHeartRateAvg >= 101 && dayHeartRateAvg <= 160) {
                rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_HIGH);
            }
            if (dayHeartRateAvg < 40) {
                rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_LOWER);
            }
            if (dayHeartRateAvg > 160) {
                rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_AVG_STATUS_HIGHER);
            }
        } else {
            rspBos.setSilentHeartAvg(0);
            rspBos.setHeartRateStatus(HealthDataConstant.HEART_RATE_STATUS_NOT_HAVE_AVG);
        }
    }

}
