package me.fruits.fruits.mapper.enums.coupon;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import me.fruits.fruits.mapper.enums.EnumLabelValue;

@Getter
public enum CategoryEnum implements EnumLabelValue {

    MERCHANT_MONEY_OFF(1,"商家满减折扣");

    /**
     * 值
     */
    @EnumValue
    private final Integer value;

    /**
     * 说明
     */
    private final String label;

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
