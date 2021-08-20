package me.fruits.fruits.service.order.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * websocket连接管理
 */
@Component
public class WebSocketSessionManage {

    //房间中的链接用户,没有资源竞争，不需要线程安全
    private Map<String, WebSocketSession> webSocketMap;

    public WebSocketSessionManage() {
        this.webSocketMap = new LinkedHashMap<>();
    }

    public void put(WebSocketSession webSocketSession) {
        webSocketMap.put(webSocketSession.getId(), webSocketSession);
    }

    public void remove(WebSocketSession webSocketSession) {
        webSocketMap.remove(webSocketSession.getId());
    }


    //广播
    public void broadcast(String msg){

        webSocketMap.forEach((k, v) -> {
            try {
                v.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
