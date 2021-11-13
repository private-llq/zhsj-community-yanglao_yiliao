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
     * 格式化LocalDateTime
     */
    public static String formatLocalDateTime(@NotNull LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
        LocalDateTime endTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth().getValue(), localDate.getDayOfMonth(), hours, minutes, second);
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
}
