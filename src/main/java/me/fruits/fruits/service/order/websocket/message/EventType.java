package me.fruits.fruits.service.order.websocket.message;

public enum EventType {
    PAY_ORDER_LIST(1, "已支付订单列表事件"),
    PAY_ORDER(2, "已支付订单事件"),
    FULFILL_ORDER_LIST(3, "制作完成订单列表事件"),
    FULFILL_ORDER(4, "制作完成订单事件");


    private int value;
    private String label;

    EventType(int value, String label) {
        this.value = value;
        this.label = label;
    }
}
