package me.fruits.fruits.configuration;

import me.fruits.fruits.controller.admin.order.OrderWebSocket;
import me.fruits.fruits.controller.admin.order.websocket.interceptor.AdminLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer  {

    @Autowired
    private OrderWebSocket orderWebSocket;
    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry  webSocketHandlerRegistry) {

        //注册一个websocket的handler
        //可以注册websocket的handler，非注解写法
        //非注解的写法有个好处是，可以注册Interceptors,监听握手和分手实际，如果认证放到握手去做，性能会高
        webSocketHandlerRegistry.addHandler(orderWebSocket,"/admin/order/orderWs")
                .addInterceptors(adminLoginInterceptor)
                .setAllowedOrigins("*");
    }

}
