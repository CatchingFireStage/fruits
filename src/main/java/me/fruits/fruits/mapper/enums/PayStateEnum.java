package me.fruits.fruits.mapper.enums;

public enum PayStateEnum implements EnumLabelValue {

    ORDER(0, "下单"),

    SUCCESS(1, "已支付"),

    FAIL(2, "支付失败");

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
