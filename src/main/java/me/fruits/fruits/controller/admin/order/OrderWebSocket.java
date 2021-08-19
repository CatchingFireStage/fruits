package me.fruits.fruits.controller.admin.order;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint("/admin/order/orderWs")
@Component
@Slf4j
public class OrderWebSocket {

    //保存所有在线socket链接
    private static Map<String, Session> webSocketMap = new LinkedHashMap<>();

    //连接加入
    @OnOpen
    public void onOpen(Session session) {
        webSocketMap.put(session.getId(), session);
        log.info("新的连接加入:{}", session.getId());
    }

    //收到信息
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息:{}", message);

        //广播消息
        this.broadcast("spring boot websocket成功,这是服务端推送信息");
    }

    //链接关闭
    @OnClose
    public void onClose(Session session) {
        webSocketMap.remove(session.getId());
        log.info("有链接断开:{}",session.getId());
    }


    //广播消息
    public void broadcast(String message) {
        webSocketMap.forEach((k, v) -> {
            try {
                v.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
