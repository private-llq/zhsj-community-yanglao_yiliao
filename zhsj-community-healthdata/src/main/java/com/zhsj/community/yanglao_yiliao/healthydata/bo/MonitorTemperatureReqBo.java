package com.zhsj.community.yanglao_yiliao.healthydata.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author zzm
 * @version 1.0
 * @Description: 检测用户体温并保存请求参数
 * @date 2021/11/11 14:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorTemperatureReqBo {

    public MonitorTemperatureReqBo(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonitorTemperatureReqBo that = (MonitorTemperatureReqBo) o;
        return createTime.equals(that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime);
    }

    /**
     * 家人id
     */
    @NotBlank(message = "家人id不能为空")
    private String familyMemberId;

    /**
     * 手腕温度
     */
    @NotNull(message = "用户手腕温度不能为空")
    private Double tmpHandler;

    /**
     * 额头温度
     */
    @NotNull(message = "用户额头温度不能为空")
    private Double tmpForehead;

    /**
     * 体温时间
     */
    @NotNull(message = "用户体温时间不能为空")
    private Long createTime;
}
