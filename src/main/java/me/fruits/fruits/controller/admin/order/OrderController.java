package me.fruits.fruits.controller.admin.order;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.service.order.OrderAdminModuleService;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@RestController(value = "AdminOrderController")
@Api(tags = "订单")
@AdminLogin
@Validated
public class OrderController {

    @Autowired
    private OrderAdminModuleService orderAdminModuleService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;


    @PatchMapping("/orderFulfill/{id}")
    @ApiOperation("订单-制作完成")
    public Result<String> orderFulfill(@PathVariable long id) {

        this.orderAdminModuleService.updateStatusToFulfill(id);

        return Result.success();
    }

    @PatchMapping("/orderDelivery/{id}")
    @ApiOperation("订单-已经送达")
    public Result<String> orderDelivery(@PathVariable long id) {
        this.orderAdminModuleService.updateStatusToDelivery(id);
        return Result.success();
    }


    @GetMapping(value = "/order")
    @ApiOperation("列表页-订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "订单状态 0下单；1：已支付；2：订单已关闭(未支付成功,支付失败); 3: 完成制作；4：已取餐", required = false)
    })
    public Result<List<Object>> order(@RequestParam(required = false) Integer state, PageVo pageVo) {
        IPage<Orders> orders = orderAdminModuleService.getOrders(state, pageVo);

        if (orders.getRecords().size() == 0) {
            return Result.success(orders.getTotal(), orders.getPages(), new ArrayList<>());
        }

        List<Object> list = new ArrayList<>();

        orders.getRecords().forEach(order -> {

            Map<String, Object> item = new HashMap<>();

            item.put("id", order.getId());
            item.put("payMoney", MoneyUtils.fenChangeYuan(order.getPayMoney()));
            item.put("state", order.getState());
            item.put("createTime", DateFormatUtils.format(order.getCreateTime()));

            try {
                item.put("description", orderService.decodeInputOrderDescriptionDTO(order.getDescription()));
            } catch (JsonProcessingException e) {
                //解码失败
                item.put("description", null);
            }

            //支付信息
            item.put("pay", null);
            Pay pay = payService.getPay(order.getId(), PayService.MerchantTransactionTypeEnum.ORDER);
            if (pay != null) {
                Map<String, Object> payData = new HashMap<>();

                payData.put("transactionId", pay.getTransactionId());
                payData.put("outTradeNo", pay.getOutTradeNo());
                payData.put("amount", MoneyUtils.fenChangeYuan(pay.getAmount()));


                item.put("pay", payData);
            }


            list.add(item);

        });

        return Result.success(orders.getTotal(), orders.getPages(), list);
    }
}
