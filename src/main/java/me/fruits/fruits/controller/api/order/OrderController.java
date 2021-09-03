package me.fruits.fruits.controller.api.order;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.ApiLogin;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.OrderApiModuleService;
import me.fruits.fruits.utils.ApiModuleRequestHolder;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/menu")
@RestController(value = "apiOrderController")
@Api(tags = "订单-订单模块")
@Validated
@ApiLogin
public class OrderController {

    @Autowired
    private OrderApiModuleService orderApiModuleService;

    @ApiOperation(value = "我的订单")
    @GetMapping("/orders")
    public Result<Object> orders(PageVo pageVo) {

        //获取我的订单
        IPage<Orders> myOrders = orderApiModuleService.getMyOrders(ApiModuleRequestHolder.get().getId(), pageVo);

        if(myOrders.getRecords().size() == 0){
            return Result.success(myOrders.getTotal(),myOrders.getPages(),null);
        }


        List<Object> response = new ArrayList<>();

        myOrders.getRecords().forEach(orders -> {

            Map<String,Object> item = new HashMap<>();

            item.put("id",orders.getId());
            item.put("payMoney",orders.getPayMoney());



            response.add(item);
        });


        return Result.success(myOrders.getTotal(),myOrders.getPages(),response);
    }
}
