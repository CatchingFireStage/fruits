package me.fruits.fruits.controller.admin.order;


import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.service.order.InputOrderDescriptionDTO;
import me.fruits.fruits.service.order.InputOrderDescriptionVO;
import me.fruits.fruits.service.order.OrderAdminModuleService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/order")
@RestController(value = "AdminOrderController")
@Api(tags = "订单")
@AdminLogin
@Validated
public class OrderController {

    @Autowired
    private OrderAdminModuleService orderAdminModuleService;

    @ApiOperation("订单-预览")
    @PostMapping("/orderPreview")
    public Result<InputOrderDescriptionDTO> orderPreview(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        InputOrderDescriptionDTO inputOrderDescriptionDTO = orderAdminModuleService.encodeInputOrderDescriptionDTO(inputOrderDescriptionVO);

        return Result.success(inputOrderDescriptionDTO);
    }

    @ApiOperation("支付-订单-Native下单API")
    @PostMapping("/payOrderByNative")
    public Result<String> addOrderByNative(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        orderAdminModuleService.addOrderByNative(inputOrderDescriptionVO);

        return Result.success();
    }

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
}
