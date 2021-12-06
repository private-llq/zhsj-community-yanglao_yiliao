package com.zhsj.community.yanglao_yiliao.healthydata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorHeartRateReqBo;
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
 * @Description: 用户心率对应实体
 * @date 2021/11/10 17:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_heart_rate")
public class HeartRate {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 当前登录用户id
     */
    private String userUuid;

    /**
     * 用户当前心率
     */
    private Integer silentHeart;

    /**
     * 用户舒张压
     */
    private Integer diastolicPressure;

    /**
     * 用户收缩压
     */
    private Integer systolicPressure;

    /**
     * 监测心率时间
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

    public static HeartRate build(@NotNull LoginUser user,
                                  @NotNull MonitorHeartRateReqBo reqBo,
                                  @NotNull LocalDateTime localDateTime) {
        return HeartRate.builder()
                .userUuid(user.getAccount())
                .silentHeart(reqBo.getSilentHeart())
                .diastolicPressure(reqBo.getDiastolicPressure())
                .systolicPressure(reqBo.getSystolicPressure())
                .createTime(localDateTime)
                .updateTime(LocalDateTime.now())
                .build();
    }
}
