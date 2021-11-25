package com.zhsj.community.yanglao_yiliao.old_activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description:
 * @create: 2021-11-22 10:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUpdateDto {
    /**
     * 性别
     */
    private  String sex;
    /**
     * 年龄
     */
    private  String age;
    /**
     * 用户昵称
     */
    private  String nickname;
}
