package me.fruits.fruits.controller.admin.order.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.service.admin.LoginAdminModuleService;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AdminLoginInterceptor implements HandshakeInterceptor {


    @Autowired
    private LoginAdminModuleService loginAdminModuleService;

    /**
     * 握手前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) {
        log.info("握手开始");

        //websocket无法自定请求头，这里用个取巧的方式，把token塞进来
        //sec-websocket-protocol请求头，是websocket允许用户自定义的协议
        //把token塞到这里面来
        List<String> protocols = serverHttpRequest.getHeaders().get("sec-websocket-protocol");

        if (protocols == null) {
            return false;
        }

        String token = protocols.get(0);
        try {
            loginAdminModuleService.verifyToken(token);

            //告诉客户端，支持改协议
            serverHttpResponse.getHeaders().set("sec-websocket-protocol",token);
            log.info("验证成功，握手成功");
            return true;

        } catch (FruitsException ignored) {

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
