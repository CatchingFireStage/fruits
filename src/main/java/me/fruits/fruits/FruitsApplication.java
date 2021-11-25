package me.fruits.fruits;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("me.fruits.fruits.mapper")
//开启多线程
@EnableAsync
//开启定时任务
@EnableScheduling
@Slf4j
public class FruitsApplication {

    public static void main(String[] args) {

        //设置时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));


        ConfigurableApplicationContext run = SpringApplication.run(FruitsApplication.class, args);

        //打印所有已经载入的bean
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        log.info("bean总数:{}", run.getBeanDefinitionCount());
        for (int i = 0; i < run.getBeanDefinitionCount(); i++) {
            log.info("第{}个,beanName:{}", i, beanDefinitionNames[i]);
        }
    }

}
