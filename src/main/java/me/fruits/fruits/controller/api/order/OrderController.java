package me.fruits.fruits.controller.api.order;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.ApiLogin;
import me.fruits.fruits.utils.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/menu")
@RestController(value = "apiOrderController")
@Api(tags = "订单-订单模块")
@Validated
@ApiLogin
public class OrderController {




    @ApiOperation(value = "经典菜单")
    @GetMapping("/orders")
    public Result<Object>  orders(){
        return null;
    }
}
