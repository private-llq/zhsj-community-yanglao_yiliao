package com.zhsj.community.yanglao_yiliao.old_activity.util;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author liulq
 * @version V1.0
 * @program: haj-community-yang_Emiliano
 * @description:
 * @create: 2021-11-11 16:00
 */
public enum SexEnum implements IEnum<Integer> {
    /**
     * 性别：男
     */
    MAN(1,"男"),
    /**
     * 性别：女
     */
    WOMAN(2,"女"),
    /**
     * I don't know
     */
    UNKNOWN(3,"未知");

    private final int value;
    private final String desc;

    SexEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
