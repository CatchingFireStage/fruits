package me.fruits.fruits.task.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 定时任务
 */
@Component
@Slf4j
public class OrderTask {

//    @Autowired
//    private OrdersMapper ordersMapper;
//
//    @Autowired
//    private OrderService orderService;
//
//    private Random random = new Random();
//
//    //fixedDelay和fixedRate，单位是毫秒
//    //cron是格式
//    @Scheduled(fixedDelay = 60 * 60 * 1000)
//    //@Async和Scheduled配置使用才能开启多线程;开启多线程，要保证函数里面的任务一定要线程安全！
//    @Async
//    public void testOrderPayAndOrderClose() {
//
//        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("state", 0);
//
//
//        List<Orders> orders = this.ordersMapper.selectList(queryWrapper);
//
//        orders.forEach(order -> {
//
//            if ((random.nextInt() % 5) == 0) {
//                //关闭订单
//                orderService.updateStatusToClose(order.getId());
//            } else {
//                //支付成功
//                orderService.updateStatusToPay(order);
//            }
//        });
//
//
//    }
}
