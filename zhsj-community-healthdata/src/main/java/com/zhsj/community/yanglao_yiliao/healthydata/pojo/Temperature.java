package com.zhsj.community.yanglao_yiliao.healthydata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.healthydata.bo.MonitorTemperatureReqBo;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 当前登录用户id
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
     * 监测体温时间
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

    public static Temperature build(@NotNull LoginUser user,
                                    @NotNull MonitorTemperatureReqBo reqBo,
                                    @NotNull LocalDateTime localDateTime) {
        return Temperature.builder()
                .userUuid(user.getAccount())
                .tmpHandler(reqBo.getTmpHandler())
                .tmpForehead(reqBo.getTmpForehead())
                .createTime(localDateTime)
                .updateTime(LocalDateTime.now())
                .build();
    }
}
