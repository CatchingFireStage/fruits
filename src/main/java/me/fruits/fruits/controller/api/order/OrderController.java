package me.fruits.fruits.controller.api.order;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.controller.ApiLogin;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.api.UserDTO;
import me.fruits.fruits.service.order.InputOrderDescriptionDTO;
import me.fruits.fruits.service.order.InputOrderDescriptionVO;
import me.fruits.fruits.service.order.OrderApiModuleService;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.utils.ApiModuleRequestHolder;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.QRCodeUtil;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/menu")
@RestController(value = "apiOrderController")
@Api(tags = "订单-订单模块")
@Validated
@ApiLogin
@Slf4j
public class OrderController {

    @Autowired
    private OrderApiModuleService orderApiModuleService;

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "我的订单")
    @GetMapping("/orders")
    public Result<Object> orders(PageVo pageVo) {

        //获取我的订单
        IPage<Orders> myOrders = orderApiModuleService.getMyOrders(ApiModuleRequestHolder.get().getId(), pageVo);

        if (myOrders.getRecords().size() == 0) {
            return Result.success(myOrders.getTotal(), myOrders.getPages(), null);
        }


        List<Object> response = new ArrayList<>();

        myOrders.getRecords().forEach(orders -> {

            Map<String, Object> item = new HashMap<>();

            item.put("id", orders.getId());
            item.put("payMoney", orders.getPayMoney());
            item.put("stateText", orderApiModuleService.changeStateToText(orders.getState()));
            try {
                item.put("description", orderService.decodeInputOrderDescriptionDTO(orders.getDescription()));
            } catch (JsonProcessingException e) {
                //跳过这条数据
                log.info("订单id：{}，DescriptionDTO json解码失败", orders.getId());
                return;
            }

            response.add(item);
        });


        return Result.success(myOrders.getTotal(), myOrders.getPages(), response);
    }


    @ApiOperation("订单-预览")
    @PostMapping("/orderPreview")
    public Result<InputOrderDescriptionDTO> orderPreview(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        UserDTO userDTO = ApiModuleRequestHolder.get();

        InputOrderDescriptionDTO inputOrderDescriptionDTO = orderService.encodeInputOrderDescriptionDTO(userDTO.getId(), inputOrderDescriptionVO);

        return Result.success(inputOrderDescriptionDTO);
    }


    @ApiOperation("支付-订单-JSAPI下单")
    @PostMapping("/payOrderByJsAPI")
    public Result<String> payOrderByJsAPI(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        UserDTO userDTO = ApiModuleRequestHolder.get();

        //下单
        String prepayId = orderService.addOrderByJSAPI(userDTO.getId(), inputOrderDescriptionVO);


        return Result.success(prepayId);
    }
}
