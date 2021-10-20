package me.fruits.fruits.configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 微信小程序配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMiniAppConfiguration {

    //配置文件
    @Autowired
    private  WxMaProperties properties;


    @Bean
    public  WxMaService weChatMiniApp() {

        //小程序配置信息
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();

        config.setAppid(properties.getAppid());
        config.setSecret(properties.getSecret());
        config.setToken(properties.getToken());
        config.setAesKey(properties.getAesKey());
        config.setMsgDataFormat(properties.getMsgDataFormat());


        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);


        return service;
    }

}
