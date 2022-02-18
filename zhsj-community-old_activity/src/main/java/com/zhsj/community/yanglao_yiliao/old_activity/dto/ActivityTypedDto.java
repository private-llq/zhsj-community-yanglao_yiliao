package com.zhsj.community.yanglao_yiliao.old_activity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @author: liulq
 * @create: 2022-01-12 16:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityTypedDto {
    /**
     * id
     */
    private String id;


    /**
     * 活动类型编码
     */
    private Integer activityTypeCode;


    /**
     * 活动类型名称
     */
    private String activityTypeName;

}
