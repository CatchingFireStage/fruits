package me.fruits.fruits.task.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
@Async
@Slf4j
public class OrderTask {

    //fixedDelay和fixedRate，单位是毫秒
    //cron是格式
    @Scheduled(fixedDelay = 2 * 1000)
    public void testOrderPayAndOrderClose(){
        log.info("使用FixedDelay  testOrderPayAndOrderClose 线程名称：{}",Thread.currentThread().getName());
    }

    @Scheduled(fixedDelay = 3 * 1000)
    public void testOrderPayAndOrderClose2(){
        log.info("使用FixedDelay  testOrderPayAndOrderClose2 线程名称：{}",Thread.currentThread().getName());
    }
}
