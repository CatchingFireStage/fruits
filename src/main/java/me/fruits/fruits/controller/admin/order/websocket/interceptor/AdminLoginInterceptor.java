package me.fruits.fruits.controller.admin.order.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class AdminLoginInterceptor implements HandshakeInterceptor {


    /**
     * 握手前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
       log.info("握手开始");


       if(1 == 1){
           log.info("验证成功，握手成功");
           return true;
       }

       log.info("验证失败，断开连接,握手失败");
        return false;
    }


    /**
     * 握手后
     *
     * @throws Exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        log.info("完成握手");
    }
}
