package com.zhsj.community.yanglao_yiliao.healthydata.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
}
