package me.fruits.fruits.mapper.enums;

/**
 * 是否有货
 */
public enum IsInventoryEnum implements EnumLabelValue {
    OFF_INVENTORY(0, "无货"),
    ON_INVENTORY(1, "有货");

    /**
     * 值
     */
    private Integer value;

    /**
     * 说明
     */
    private String label;


    IsInventoryEnum(Integer value, String label) {
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
