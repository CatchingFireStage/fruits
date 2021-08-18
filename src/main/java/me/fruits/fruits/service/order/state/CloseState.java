package me.fruits.fruits.service.order.state;

/**
 * 什么情况下会关闭订单?
 *
 微信： 以下情况需要调用关单接口：
 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。

 总结：就是订单未支付成功需要关闭订单; 在微信的异步支付结果通知中可以检测到上述场景
 */
public class CloseState implements State{

    @Override
    public void doAction(Context context) {

        //当前状态是关闭订单状态
        context.setState(this);

        context.getOrderService().updateStatusToClose(context.getOrder().getId());
    }
}
