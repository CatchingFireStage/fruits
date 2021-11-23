package me.fruits.fruits.mapper.enums.pay;


import me.fruits.fruits.mapper.enums.EnumLabelValue;

public enum PayStateEnum implements EnumLabelValue {

    ORDER(0, "下单"),

    SUCCESS(1, "已支付"),

    FAIL(2, "支付失败"),

    REFUND(3, "进入退款,有一条或者多条退款信息");

    /**
     * 值
     */
    private Integer value;

    /**
     * 说明
     */
    private String label;


    PayStateEnum(Integer value, String label) {
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
