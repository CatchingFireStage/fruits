package me.fruits.fruits;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("me.fruits.fruits.mapper")
public class FruitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitsApplication.class, args);
    }

}
