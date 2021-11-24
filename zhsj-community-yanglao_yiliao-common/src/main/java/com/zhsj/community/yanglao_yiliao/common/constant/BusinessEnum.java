package com.zhsj.community.yanglao_yiliao.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: 业务相关枚举
 * @author: Hu
 * @create: 2021-11-10 14:13
 **/
public interface BusinessEnum {

    /**
     * 资源类查询类型大字典
     */
    Map<String, List<Map<String, Object>>> sourceMap = new HashMap<>();

    /**
     * @Description: 家人档案关系
     * @author: Hu
     * @since: 2021/11/10 14:16
     * @Param:
     * @return:
     */
    enum FamilyRelationTextEnum {
        /**
         * 父亲
         */
        FATHER("父亲", 1),
        /**
         * 母亲
         */
        MOTHER("母亲", 2),
        /**
         * 妻子
         */
        WIFE("妻子", 3),
        /**
         * 丈夫
         */
        hubby("丈夫", 4),
        /**
         * 儿子
         */
        SON("儿子", 5),
        /**
         * 女儿
         */
        GIRL("女儿", 6),
        /**
         * 爷爷
         */
        GRANDPA("爷爷", 7),
        /**
         * 奶奶
         */
        GRANDMA("奶奶", 8),
        /**
         * 孙子
         */
        GRANDSON("孙子", 9),

        /**
         * 孙女
         */
        GRANDDAUGHTER("孙女", 10),
        /**
         * 其他
         */
        ELSE("其他", 11);

        /**
         * 描述
         */
        private String name;
        /**
         * 编号
         */
        private Integer code;

        FamilyRelationTextEnum(String name, Integer code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return this.code + "_" + this.name;
        }

        public static final List<Map<String, Object>> familyRelationTextList = new ArrayList<>();
        public static String getName(Integer code) {
            FamilyRelationTextEnum[] values = FamilyRelationTextEnum.values();
            for (FamilyRelationTextEnum c : values) {
                if (c.code.equals(code)) {
                    return c.name;
                }
            }
            return null;
        }
        static {
            for (FamilyRelationTextEnum feeRuleNameEnum : FamilyRelationTextEnum.values()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("code", feeRuleNameEnum.getCode());
                map.put("name", feeRuleNameEnum.getName());
                familyRelationTextList.add(map);
            }
            sourceMap.put("familyRelationText",familyRelationTextList);
        }
    }

}

