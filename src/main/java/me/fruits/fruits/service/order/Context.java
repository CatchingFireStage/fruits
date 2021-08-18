package me.fruits.fruits.service.order;


/**
 * 使用状态模式来生成订单
 */
public class Context {

    //当前上下文的状态
    private State state;

    public Context() {
        state = null;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
