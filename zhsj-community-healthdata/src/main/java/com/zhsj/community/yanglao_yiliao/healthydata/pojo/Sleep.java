package com.zhsj.community.yanglao_yiliao.healthydata.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorSleepReqBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户睡眠对应实体
 * @date 2021/11/11 15:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_sleep")
public class Sleep {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 当前登录用户id
     */
    private String userUuid;

    /**
     * 睡眠计数步数
     */
    private Integer stepCount;

    /**
     * 睡眠状态（1 开始入睡 2 浅睡 3 深睡 4 清醒 5 快速眼动）
     */
    private Integer sleepStatus;

    /**
     * 监测睡眠时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 是否删除（0：已删除false，1：未删除true）
     */
    @TableLogic
    private Boolean deleted;

    public static Sleep build(@NotNull LoginUser user,
                              @NotNull MonitorSleepReqBo reqBo,
                              @NotNull LocalDateTime localDateTime) {
        return Sleep.builder()
                .userUuid(user.getAccount())
                .stepCount(reqBo.getStepCount())
                .sleepStatus(reqBo.getSleepStatus())
                .createTime(localDateTime)
                .updateTime(LocalDateTime.now())
                .build();
    }
}
