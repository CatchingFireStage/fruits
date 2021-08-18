package me.fruits.fruits.service.order;


/**
 * 使用状态模式来生成订单
 */
public class Context {

    //当前上下文的状态
    private State state;

    //订单服务
    private OrderService orderService;


    //订单详情
    private InputOrderDescriptionDTO inputOrderDescriptionDTO;


    public Context(OrderService orderService) {
        state = null;
        this.orderService = orderService;
    }


    public InputOrderDescriptionDTO getInputOrderDescriptionDTO() {
        return inputOrderDescriptionDTO;
    }

    public void setInputOrderDescriptionDTO(InputOrderDescriptionDTO inputOrderDescriptionDTO) {
        this.inputOrderDescriptionDTO = inputOrderDescriptionDTO;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
