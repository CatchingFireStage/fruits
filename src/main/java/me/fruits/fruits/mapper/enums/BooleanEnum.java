package me.fruits.fruits.mapper.enums;


public enum BooleanEnum implements EnumLabelValue {

    FALSE(0, "不存在，没有,不可以"),
    TRUE(1, "存在，有，可以");


    /**
     * 值
     */
    private Integer value;

    /**
     * 说明
     */
    private String label;


    BooleanEnum(Integer value, String label) {
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
