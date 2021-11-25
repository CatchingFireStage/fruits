package me.fruits.fruits.listen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.TimeZone;


/**
 * web服务器启动完整之后的监听器
 */
@Component
@Slf4j
public class ServerListener implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        log.info("当前程序启动监听的端口:{}", webServerInitializedEvent.getWebServer().getPort());
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+:08:00"));
        log.info("设置系统的TimeZone:{}", "GMT+:08:00");
    }
}
