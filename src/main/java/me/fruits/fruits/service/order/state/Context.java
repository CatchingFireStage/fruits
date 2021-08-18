package me.fruits.fruits.service.order.state;


import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.InputOrderDescriptionDTO;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.service.order.state.State;

/**
 * 使用状态模式来生成订单
 */
public class Context {

    //当前上下文的状态
    private State state;


    //订单服务
    private OrderService orderService;


    //数据库中的订单
    private Orders orders;

    //订单详情
    private InputOrderDescriptionDTO inputOrderDescriptionDTO;



    public Context(OrderService orderService, Orders orders, InputOrderDescriptionDTO inputOrderDescriptionDTO) {
        state = null;
        this.orderService = orderService;
        this.orders = orders;
        this.inputOrderDescriptionDTO = inputOrderDescriptionDTO;
    }


    public Orders getOrder() {
        return orders;
    }

    public InputOrderDescriptionDTO getInputOrderDescriptionDTO() {
        return inputOrderDescriptionDTO;
    }


    public OrderService getOrderService() {
        return orderService;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
