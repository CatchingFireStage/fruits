package me.fruits.fruits.mapper.enums.pay;


import me.fruits.fruits.mapper.enums.EnumLabelValue;

//支付的订单类型
public enum MerchantTransactionTypeEnum implements EnumLabelValue {

    ORDER(1, "订单"),
    RECHARGE(2, "充值");

    private int value;

    private String label;

    MerchantTransactionTypeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getLabel() {
        return this.label;
    }
}