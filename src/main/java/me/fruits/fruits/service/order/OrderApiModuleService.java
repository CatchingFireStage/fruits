package me.fruits.fruits.service.order;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.enums.orders.OrderStateEnum;
import me.fruits.fruits.mapper.enums.pay.MerchantTransactionTypeEnum;
import me.fruits.fruits.mapper.enums.pay.PayStateEnum;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderApiModuleService {


    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private PayService payService;


    /**
     * 获取我的订单列表
     *
     * @param userId 用户id
     */
    public IPage<Orders> getMyOrders(long userId, PageVo pageVo) {

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        //只能看1已支付，3制作完成, 4已取餐

        queryWrapper.in("state", Arrays.asList(OrderStateEnum.PAY.getValue(), OrderStateEnum.COMPLETED.getValue(), OrderStateEnum.DELIVERY.getValue()));


        queryWrapper.orderByDesc("create_time");

        return ordersMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }


    /**
     * 显示前端展示订单的状态
     *
     * @param order 订单数据
     */
    public String showStateText(Orders order) {
        Integer state = order.getState();


        if (OrderStateEnum.ORDER.getValue().equals(state)) {
            //未支付的情况

            return "待支付";
        }

        if (OrderStateEnum.CLOSE.getValue().equals(state)) {
            return "订单关闭，支付失败";
        }

        //支付成功处理

        String stateText = "支付成功";

        if (OrderStateEnum.PAY.getValue().equals(state)
                || OrderStateEnum.COMPLETED.getValue().equals(state)
        ) {
            stateText = "制作中";
        }

        if (OrderStateEnum.DELIVERY.getValue().equals(state)) {
            stateText = "已完成";
        }


        //查看订单是否处理了退款
        Pay pay = payService.getPay(order.getId(), MerchantTransactionTypeEnum.ORDER);

        if (PayStateEnum.REFUND.getValue().equals(pay.getState())) {
            stateText = "已退款";
        }

        return stateText;


    }


}
