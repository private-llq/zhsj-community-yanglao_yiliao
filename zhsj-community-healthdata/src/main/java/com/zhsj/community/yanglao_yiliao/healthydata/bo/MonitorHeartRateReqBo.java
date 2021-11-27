package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author zzm
 * @version 1.0
 * @Description: 监测并保存心率请求参数
 * @date 2021/11/11 9:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorHeartRateReqBo {

    public MonitorHeartRateReqBo(Long time) {
        this.createTime = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonitorHeartRateReqBo that = (MonitorHeartRateReqBo) o;
        return Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime);
    }

    /**
     * 用户心率
     */
    @NotNull(message = "用户心率不能为空")
    private Integer silentHeart;

    /**
     * 用户舒张压
     */
    @NotNull(message = "用户舒张压不能为空")
    private Integer diastolicPressure;

    /**
     * 用户收缩压
     */
    @NotNull(message = "用户收缩压不能为空")
    private Integer systolicPressure;

    /**
     * 心率时间
     */
    @NotNull(message = "心率时间不能为空")
    private Long createTime;
}


