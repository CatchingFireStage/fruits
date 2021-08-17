package me.fruits.fruits.utils;

import java.math.BigDecimal;

/**
 * 金额的工具类
 */
public class MoneyUtils {

    /**
     * 单位元转化为单位分
     */
    public static int yuanChangeFen(String yuan){

        if(yuan == null || yuan.trim().equals("")){
            return 0;
        }

        BigDecimal bigDecimal = new BigDecimal(yuan).setScale(2);
        return bigDecimal.multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 分转化为元
     */
    public static String fenChangeYuan(int fen){
        return BigDecimal.valueOf(Long.valueOf(fen)).divide(new BigDecimal(100)).toString();
    }
}
