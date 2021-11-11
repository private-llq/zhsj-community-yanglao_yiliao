package com.zhsj.community.yanglao_yiliao.healthydata.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorTemperatureReqBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zzm
 * @version 1.0
 * @Description: 用户温度对应实体
 * @date 2021/11/11 13:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_temperature")
public class Temperature {

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
     * 手腕温度
     */
    private Double tmpHandler;

    /**
     * 额头温度
     */
    private Double tmpForehead;

    /**
     * 体温时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除（0：已删除false，1：未删除true）
     */
    @TableLogic
    private Boolean deleted;

    public static Temperature build(LoginUser user, MonitorTemperatureReqBo reqBo, LocalDateTime localDateTime) {
        return Temperature.builder()
                .userUuid(user.getAccount())
                .tmpHandler(reqBo.getTmpHandler())
                .tmpForehead(reqBo.getTmpForehead())
                .createTime(localDateTime)
                .build();
    }
}
