package com.zhsj.community.yanglao_yiliao.common.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zzm
 * @version 1.0
 * @Description: 日志实体
 * @date 2021/11/9 17:23
 */
@Data
public class SystemLog {

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 日志类型
     */
    private String logLevel;

    /**
     * 创建用户编码
     */
    private String createUserCode;

    /**
     * 请求描述
     */
    private String description;

    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求服务器地址
     */
    private String serverAddress;

    /**
     * 是否异常
     */
    private String isException;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 执行时间
     */
    private Long executeTime;
}
