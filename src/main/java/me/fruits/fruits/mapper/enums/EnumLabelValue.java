package me.fruits.fruits.mapper.enums;

public interface EnumLabelValue<T> {


    /**
     * 获取枚举说明
     */
    public String getLabel();

    /**
     * 获取枚举值
     */
    public Integer getValue();

    /**
     * 通过整形转化成枚举
     */
    public T valueOf(int enumValue);

}
