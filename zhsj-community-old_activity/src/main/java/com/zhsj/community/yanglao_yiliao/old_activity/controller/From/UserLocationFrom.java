package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;

import cn.hutool.core.lang.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author liulq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationFrom implements Serializable {

    private static final long serialVersionUID = 4508868382007529970L;

    @Id
    private ObjectId id;
    @Indexed
    private Long userId; //用户id
//    private GeoJsonPoint location; //x:经度 y:纬度
    @NotNull(message = "经度不能为空")
     private Double longitude;//x:经度 y:纬度
    @NotNull(message = "维度不能为空")
    private Double latitude; //维度
    @NotNull(message = "位置描述不能为空")
    private String address; //位置描述
    private Long created; //创建时间
    private Long updated; //更新时间

}
