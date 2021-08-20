package me.fruits.fruits.controller.admin.order;


import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.service.order.websocket.WebSocketSessionManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@Slf4j
public class OrderWebSocket extends TextWebSocketHandler {


    @Autowired
    private OrderService orderService;

    @Autowired
    private WebSocketSessionManage webSocketSessionManage;

    /**
     * 连接加入
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        webSocketSessionManage.put(session);


        //支付订单列表事件
        session.sendMessage(new TextMessage(orderService.buildWebsocketPayOrderListEvent()));

        //发送制作完成订单列表事件
        session.sendMessage(new TextMessage(orderService.buildWebsocketFulfillOrderListEvent()));

        log.info("新的连接加入:{}", session.getId());
    }


    /**
     * 接受到消息
     */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("收到消息:{}", message);
        
        //广播消息
        webSocketSessionManage.broadcast("服务器目前不接受客户端任何消息的处理，只有服务端推送");
    }

    /**
     * socket 断开连接时
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessionManage.remove(session);
        log.info("有链接断开:{}", session.getId());
    }

}
