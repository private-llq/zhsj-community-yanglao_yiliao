package com.zhsj.community.yanglao_yiliao.old_activity.controller.From;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description:
 * @create: 2021-11-11 18:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityFrom {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 活动类型编码
     */
    private String activityTypeCode;


    /**
     * 活动类型名称
     */
    private String activityTypeName;

}
