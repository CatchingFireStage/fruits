package me.fruits.fruits.configuration;

import me.fruits.fruits.utils.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * 一些项目，工具类的自动注入
 */
public class FruitsApplicationConfiguration {

    /**
     * 雪花算法注入,分布式唯一id
     *
     * @return
     */
    @Bean
    public SnowFlake snowFlake() {

        return new SnowFlake(1, 1);
    }
}
