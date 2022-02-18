package me.fruits.fruits.mapper.enums.coupon;

import com.baomidou.mybatisplus.annotation.EnumValue;
import me.fruits.fruits.mapper.enums.EnumLabelValue;

public enum CategoryEnum implements EnumLabelValue {

    MERCHANT_MONEY_OFF(1,"商家满减折扣");

    /**
     * 值
     */
    @EnumValue
    private Integer value;

    /**
     * 说明
     */
    private String label;

    CategoryEnum(Integer value, String label) {
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
