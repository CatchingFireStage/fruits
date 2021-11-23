package me.fruits.fruits.utils;

import me.fruits.fruits.mapper.enums.EnumLabelValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举工具类
 */
public class EnumUtils {


    /**
     * @param myEnum 枚举类
     * @param value  枚举value值
     * @return 返回枚举对应的字符串
     */
    public static String changeToString(Class myEnum, int value) {

        //验证是否实现该接口
        if (!EnumLabelValue.class.isAssignableFrom(myEnum)) {
            throw new FruitsRuntimeException("枚举没有实现 EnumLabelValue接口");
        }

        try {

            Method getValue = myEnum.getMethod("getValue");

            Object[] enumConstants = myEnum.getEnumConstants();

            for (int i = 0; i < enumConstants.length; i++) {

                Integer enumValue = (Integer) getValue.invoke(enumConstants[i]);

                if (enumValue.equals(value)) {

                    //返回枚举的名字
                    return enumConstants[i].toString();
                }

            }


        } catch (NoSuchMethodException e) {
            e.printStackTrace();

            throw new FruitsRuntimeException("枚举反射找不到方法");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new FruitsRuntimeException("枚举反射调用方法出错");
        }

        throw new FruitsRuntimeException("枚举反射，未知异常");
    }
}
