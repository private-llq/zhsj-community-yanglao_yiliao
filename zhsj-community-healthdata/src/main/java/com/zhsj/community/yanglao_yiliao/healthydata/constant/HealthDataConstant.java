package com.zhsj.community.yanglao_yiliao.healthydata.constant;

/**
 * @author zzm
 * @version 1.0
 * @Description: 健康数据常量
 * @date 2021/11/12 18:34
 */
public class HealthDataConstant {

    //--------------------------------------------------health color status--------------------------------------------------------------------

    /**
     * 健康颜色状态1：绿色（健康）
     */
    public static final Integer HEALTH_COLOR_STATUS_GREEN = 1;
    /**
     * 健康颜色状态2：黄色（亚健康）
     */
    public static final Integer HEALTH_COLOR_STATUS_YELLOW = 2;
    /**
     * 健康颜色状态3：红色（危险）
     */
    public static final Integer HEALTH_COLOR_STATUS_RED = 3;

    //--------------------------------------------------sleep---------------------------------------------------------------

    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    public static final Integer SLEEP_STATUS_ONE = 1;
    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    public static final Integer SLEEP_STATUS_TWO = 2;
    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    public static final Integer SLEEP_STATUS_THREE = 3;
    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    public static final Integer SLEEP_STATUS_FOUR = 4;
    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    public static final Integer SLEEP_STATUS_FIVE = 5;
    /**
     * 抓取睡眠今天截止的时间点
     */
    public static final Integer GRAB_SLEEP_TIME_ELEVEN = 11;
    /**
     * 抓取睡眠昨天开始的时间点
     */
    public static final Integer GRAB_SLEEP_TIME_TWENTY_ONE = 21;
    /**
     * 抓取睡眠时间的分秒值
     */
    public static final Integer GRAB_SLEEP_TIME_MINUTE_SECOND = 0;
    /**
     * 每十分钟抓取一次睡眠
     */
    public static final Integer GRAB_SLEEP_TIME_STEP = 10;


    //---------------------------------------------chart time-------------------------------------------------------------------

    /**
     * 查询健康图表数据时间类型（以天查）
     */
    public static final String HEALTH_DATA_SELECT_CHART_TIME_DAY = "day";
    /**
     * 查询健康图表数据时间类型（以周查）
     */
    public static final String HEALTH_DATA_SELECT_CHART_TIME_WEEK = "week";
    /**
     * 查询健康图表数据时间类型（以月查）
     */
    public static final String HEALTH_DATA_SELECT_CHART_TIME_MONTH = "month";
}
