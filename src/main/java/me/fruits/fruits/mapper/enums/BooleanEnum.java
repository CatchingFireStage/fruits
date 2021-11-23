package me.fruits.fruits.mapper.enums;

import me.fruits.fruits.utils.FruitsRuntimeException;

public enum BooleanEnum implements EnumLabelValue<BooleanEnum> {

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

    @Override
    public BooleanEnum valueOf(int enumValue) {

        if (BooleanEnum.TRUE.getValue().equals(enumValue)) {
            return BooleanEnum.TRUE;
        }

        if (BooleanEnum.FALSE.getValue().equals(enumValue)) {
            return BooleanEnum.FALSE;
        }

        throw new FruitsRuntimeException("找不到枚举");
    }


}
