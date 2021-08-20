package me.fruits.fruits;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("me.fruits.fruits.mapper")
//开启多线程
@EnableAsync
//开启定时任务
@EnableScheduling
public class FruitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitsApplication.class, args);
    }

}
