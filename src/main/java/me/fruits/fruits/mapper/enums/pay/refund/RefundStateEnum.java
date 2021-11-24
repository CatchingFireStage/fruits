package me.fruits.fruits.mapper.enums.pay.refund;

import me.fruits.fruits.mapper.enums.EnumLabelValue;

public enum RefundStateEnum implements EnumLabelValue {

    REFUND(0, "退款中"),

    SUCCESS(1, "退款成功"),

    /**
     * 如果监听到微信传过来的消息退款结果是 ABNORMAL、CLOSE， 说明没有退款成功，需要基于原来的refund数据记录重新发出退款申请
     */
    ABNORMAL(2, "退款异常，需要去微信商户平台手动处理"),

    CLOSE(3, "退款关闭");

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
