package com.zhsj.community.yanglao_yiliao.old_activity.util;

/**
 * @author liulq
 * @version V1.0
 * @program: zhsj-community-yanglao_yiliao
 * @description: 生成随机数
 * @create: 2021-11-11 10:12
 */
public class RandomUtils {

    private static final String CHARLIST = "0123456789";

    public static String createRandomString(int len) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            str.append(CHARLIST.charAt(getRandom(CHARLIST.length())));
        }
        return str.toString();
    }

    public static int getRandom(int mod) {
        if (mod < 1) {
            return 0;
        }
        return getInt() % mod;
    }

    private static int getInt() {
        return Math.abs(Long.valueOf(getRandomNumString()).intValue());
    }

    private static String getRandomNumString() {
        double d = Math.random();
        String dStr = String.valueOf(d).replaceAll("[^\\d]", "");
        if (dStr.length() > 1) {
            dStr = dStr.substring(0, dStr.length() - 1);
        }
        return dStr;
    }
}
