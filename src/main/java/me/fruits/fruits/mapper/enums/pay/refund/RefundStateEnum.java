package me.fruits.fruits.mapper.enums.pay.refund;

import me.fruits.fruits.mapper.enums.EnumLabelValue;

public enum RefundStateEnum implements EnumLabelValue {

    REFUND(0, "退款中"),

    SUCCESS(1, "退款成功");

    /**
     * 值
     */
    private Integer value;

    /**
     * 说明
     */
    private String label;


    RefundStateEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
