package me.fruits.fruits.service.order.state;


import me.fruits.fruits.mapper.po.Order;
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
    private Order order;

    //订单详情
    private InputOrderDescriptionDTO inputOrderDescriptionDTO;



    public Context(OrderService orderService, Order order, InputOrderDescriptionDTO inputOrderDescriptionDTO) {
        state = null;
        this.orderService = orderService;
        this.order = order;
        this.inputOrderDescriptionDTO = inputOrderDescriptionDTO;
    }


    public Order getOrder() {
        return order;
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
