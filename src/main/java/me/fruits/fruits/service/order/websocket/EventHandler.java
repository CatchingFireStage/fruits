package me.fruits.fruits.service.order.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingDeque;


/**
 * order模块 websocket通知前端的消息
 */
@Service
@Slf4j
public class EventHandler {


    @Autowired
    private OrderService orderService;

    //有新的支付订单，LinkedBlockingDeque，默认值容量值是int类型最大值，初始化为100个容量，，防止消费者更不上生产者，导致内存满了，然后程序挂掉了
    private static LinkedBlockingDeque<String> newPayOrderNotifyQueue = new LinkedBlockingDeque<>(100);

    //有制作完成的订单
    private static LinkedBlockingDeque<String> newFulfillOrderNotifyQueue = new LinkedBlockingDeque<>(100);


    //开启多线程
    //新的支付订单消费者
    @Async
    public void consumeNewPayOrderNotify() {

        while (true) {
            //会阻塞
            try {
                String msg = newPayOrderNotifyQueue.take();
                //todo 消费信息
            } catch (InterruptedException exception) {

                //抛出中断异常，释放占用的线程
                exception.printStackTrace();
                return;
            }
        }

    }

    //新的支付订单生产者
    public void producerNewPayOrderNotify(Orders orders) {

        try {
            String msg = orderService.buildWebsocketPayOrderEvent(orders);
            newPayOrderNotifyQueue.add(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void consumeNewFulfillOrderNotify() {
        while (true) {
            try {

                String msg = newFulfillOrderNotifyQueue.take();
                //todo 消费信息

            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    //新的制作完成订单生成者
    public void producerNewFulfillOrderNotify(Orders orders) {
        try {
            String msg = orderService.buildWebsocketFulfillOrderEvent(orders);
            newFulfillOrderNotifyQueue.add(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
