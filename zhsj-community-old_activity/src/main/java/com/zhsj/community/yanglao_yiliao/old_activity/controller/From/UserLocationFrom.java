package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;

import cn.hutool.core.lang.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;


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
     private Double longitude;//x:经度 y:纬度
    private Double latitude; //维度
    private String address; //位置描述
    private Long created; //创建时间
    private Long updated; //更新时间

}
