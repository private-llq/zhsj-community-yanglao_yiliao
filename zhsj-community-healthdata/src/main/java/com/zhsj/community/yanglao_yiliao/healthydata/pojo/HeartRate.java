package com.zhsj.community.yanglao_yiliao.healthydata.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorHeartRateReqBo;
import com.zhsj.community.yanglao_yiliao.healthydata.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @TableId
    private Long id;
    /**
     * 用户id
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
     * 检测心率时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public static HeartRate build(LoginUser user, MonitorHeartRateReqBo reqBo) {
        return HeartRate.builder()
                .userUuid(user.getAccount())
                .silentHeart(reqBo.getSilentHeart())
                .diastolicPressure(reqBo.getDiastolicPressure())
                .systolicPressure(reqBo.getSystolicPressure())
                .createTime(TimeUtils.formatTimestamp(reqBo.getCreateTime()))
                .build();
    }
}
