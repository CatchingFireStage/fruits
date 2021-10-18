package me.fruits.fruits.controller.admin.order;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.InputOrderDescriptionDTO;
import me.fruits.fruits.service.order.InputOrderDescriptionVO;
import me.fruits.fruits.service.order.OrderAdminModuleService;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ApiOperation("订单-预览")
    @PostMapping("/orderPreview")
    public Result<InputOrderDescriptionDTO> orderPreview(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        InputOrderDescriptionDTO inputOrderDescriptionDTO = orderAdminModuleService.encodeInputOrderDescriptionDTO(inputOrderDescriptionVO);

        return Result.success(inputOrderDescriptionDTO);
    }

    @ApiOperation("支付-订单-Native下单API")
    @PostMapping("/payOrderByNative")
    public Result<String> addOrderByNative(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        //下单
        String payQRCode = orderAdminModuleService.addOrderByNative(inputOrderDescriptionVO);


        //返回二维码
        String base64QRCode = QRCodeUtil.getBase64QRCode(payQRCode);

        return Result.success(base64QRCode);
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


    @GetMapping(value = "/order")
    @ApiOperation("列表页-订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "订单状态", required = false, example = "0下单；1：已支付；2：订单已关闭(未支付成功,支付失败); 3: 完成制作；4：已取餐")
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

            try {
                item.put("description", orderService.decodeInputOrderDescriptionDTO(order.getDescription()));
            } catch (JsonProcessingException e) {
                //解码失败
                item.put("description", null);
            }

            item.put("createTime", DateFormatUtils.format(order.getCreateTime()));


            list.add(item);

        });

        return Result.success(orders.getTotal(), orders.getPages(), list);
    }
}
