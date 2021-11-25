package me.fruits.fruits.mapper.enums.user.weChat;

import me.fruits.fruits.mapper.enums.EnumLabelValue;

public enum CarrierEnum implements EnumLabelValue {
    MINI_PROGRAM(0, "小程序"),
    GZH(1, "公众号");


    private int value;
    private String label;

    CarrierEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }


    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
