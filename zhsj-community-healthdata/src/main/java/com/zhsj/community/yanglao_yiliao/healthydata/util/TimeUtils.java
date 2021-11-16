package com.zhsj.community.yanglao_yiliao.healthydata.util;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author zzm
 * @version 1.0
 * @Description: 时间格式化工具类
 * @date 2021/11/11 11:04
 */
public class TimeUtils {

    /**
     * 格式化时间戳->LocalDateTime
     */
    public static LocalDateTime formatTimestamp(@NotNull Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = localDateTime.format(formatter);
        return LocalDateTime.parse(format, formatter);
    }

    /**
     * 格式化LocalDateTime yyyy-MM-dd HH:mm:ss
     */
    public static String formatLocalDateTime(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    /**
     * 格式化LocalDateTime yyyy/MM/dd
     */
    public static String formatLocalDateTimeSecond(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return localDateTime.format(formatter);
    }

    /**
     * 格式化LocalDateTime HH:mm
     */
    public static String formatLocalDateTimeThird(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * 格式化LocalDateTime MM月dd日 HH:mm
     */
    public static String formatLocalDateTimeFourth(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日 HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * 格式化LocalDateTime yyyy年MM月dd日
     */
    public static String formatLocalDateTimeFifth(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        return localDateTime.format(formatter);
    }

    /**
     * 格式化LocalDateTime MM/dd
     */
    public static String formatLocalDateTimeSixth(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        return localDateTime.format(formatter);
    }

    /**
     * 比较当前系统时间跟传入的时间比较时间先后
     */
    public static boolean isBefore(@NotNull Integer hours,
                                   @NotNull Integer minutes,
                                   @NotNull Integer second) {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDate localDate = nowTime.toLocalDate();
        LocalDateTime endTime = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hours, minutes, second);
        return endTime.isBefore(nowTime);
    }

    /**
     * 获取指定时间的LocalDateTime
     */
    public static LocalDateTime buildLocalDateTime(@NotNull Integer years,
                                                   @NotNull Integer months,
                                                   @NotNull Integer days,
                                                   @NotNull Integer hours,
                                                   @NotNull Integer minutes,
                                                   @NotNull Integer second) {
        return LocalDateTime.of(years, months, days, hours, minutes, second);
    }


    public static LocalDateTime ttt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        LocalDateTime toDayZeroClock = buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);


        return null;
    }

    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDate localDate = now.toLocalDate();
//        int hour = now.getHour();
//        int minute = now.getMinute();
//        int second = now.getSecond();
//        ArrayList<LocalDateTime> arr = new ArrayList<>();
//        for (int a = 0; a <= hour; a++) {
//            LocalDateTime clock = buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), a, 0, 0);
//            arr.add(clock);
//            if (minute / 10 != 0 && a == hour) {
//                LocalDateTime nowClock = buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), a, minute, second);
//                arr.add(nowClock);
//            }
//        }
//        System.out.println("===================>" + arr.toString());
//
//
//        LocalDateTime localDateTime = buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
//        LocalDateTime localDateTime1 = localDateTime.plusDays(-6);
//        System.out.println(localDateTime1);
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDate localDate = now.toLocalDate();
//        LocalDateTime todayZeroClock = TimeUtils.buildLocalDateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
//        LocalDateTime todayZeroClockSixHoursAgo = todayZeroClock.plusHours(-6);
//        LocalDateTime todaySixClock = todayZeroClock.plusHours(6);
//        LocalDateTime todayTwelveClock = todayZeroClock.plusHours(12);
//        LocalDateTime todayEighteenClock = todayZeroClock.plusHours(18);
//        LocalDateTime todayTwentyFourClock = todayZeroClock.plusHours(24);
//
//        System.out.println(now.compareTo(todayZeroClock));
//        System.out.println(now.compareTo(todayTwentyFourClock));
    }
}
