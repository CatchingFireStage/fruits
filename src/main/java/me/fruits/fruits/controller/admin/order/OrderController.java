package me.fruits.fruits.controller.admin.order;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.utils.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController(value = "AdminOrderController")
@Api(tags = "订单")
@AdminLogin
@Validated
public class OrderController {


    @ApiOperation("订单-创建")
    @PostMapping("/order")
    public Result<String> order(){
        return Result.success();
    }
}
