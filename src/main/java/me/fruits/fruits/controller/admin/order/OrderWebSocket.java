package me.fruits.fruits.controller.admin.order;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class OrderWebSocket extends TextWebSocketHandler {

    //保存所有在线socket链接
    private static Map<String, WebSocketSession> webSocketMap = new LinkedHashMap<>();

    /**
     * 连接加入
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        webSocketMap.put(session.getId(), session);

        //广播消息
        session.sendMessage(new TextMessage("欢迎"));
        log.info("新的连接加入:{}", session.getId());
    }


    /**
     * 接受到消息
     */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("收到消息:{}", message);

        //广播消息
        this.broadcast("spring boot websocket成功,这是服务端推送信息");
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketMap.remove(session.getId());
        log.info("有链接断开:{}", session.getId());
    }

    
    //广播消息
    private void broadcast(String message) {
        webSocketMap.forEach((k, v) -> {
            try {
                v.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
