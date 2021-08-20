package me.fruits.fruits.listen.admin;

import me.fruits.fruits.service.order.websocket.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OrdersWebsocketApplicationRunner implements ApplicationRunner {


    @Autowired
    private EventHandler eventHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //异步任务，不会阻塞
        eventHandler.consumeNewPayOrderNotify();

        eventHandler.consumeNewFulfillOrderNotify();
    }
}
