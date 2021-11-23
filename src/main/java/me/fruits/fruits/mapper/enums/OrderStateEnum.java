package me.fruits.fruits.mapper.enums;


public enum OrderStateEnum implements EnumLabelValue {

    ORDER(0, "下单"),
    PAY(1, "已支付"),
    CLOSE(2, "订单关闭（未支付成功，支付失败）"),
    COMPLETED(3, "制作完成"),
    DELIVERY(4, "已取餐");


    /**
     * 值
     */
    private Integer value;

    /**
     * 说明
     */
    private String label;


    OrderStateEnum(Integer value, String label) {
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
