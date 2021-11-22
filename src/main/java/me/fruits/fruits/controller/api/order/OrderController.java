package me.fruits.fruits.controller.api.order;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
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
import me.fruits.fruits.utils.*;
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
    public Result<Object> orderPreview(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        UserDTO userDTO = ApiModuleRequestHolder.get();

        InputOrderDescriptionDTO inputOrderDescriptionDTO = orderService.encodeInputOrderDescriptionDTO(userDTO.getId(), inputOrderDescriptionVO);


        //响应
        Map<String, Object> response = new HashMap<>();


        //订单详情
        List<Object> orderDescription = new ArrayList<>();

        inputOrderDescriptionDTO.getOrderDescription().forEach(orderDescriptionDTO -> {

            Map<String, Object> orderDescriptionItemRow = new HashMap<>();

            //spu
            Map<String, Object> spu = new HashMap<>();

            spu.put("money", MoneyUtils.fenChangeYuan(orderDescriptionDTO.getSpu().getMoney()));
            spu.put("name", orderDescriptionDTO.getSpu().getName());

            orderDescriptionItemRow.put("spu", spu);

            //spu的规格值
            List<Object> spuSpecificationValue = new ArrayList<>();

            orderDescriptionDTO.getSpuSpecificationValue().forEach(spuSpecificationValueItem -> {

                Map<String, Object> spuSpecificationValueItemRow = new HashMap<>();

                spuSpecificationValueItemRow.put("money", MoneyUtils.fenChangeYuan(spuSpecificationValueItem.getMoney()));
                spuSpecificationValueItemRow.put("name", spuSpecificationValueItem.getName());
                spuSpecificationValueItemRow.put("value", spuSpecificationValueItem.getValue());


                spuSpecificationValue.add(spuSpecificationValueItemRow);
            });


            orderDescriptionItemRow.put("spuSpecificationValue", spuSpecificationValue);

            orderDescription.add(orderDescriptionItemRow);

        });


        response.put("orderDescription", orderDescription);

        //订单总金额
        response.put("payAmount", MoneyUtils.fenChangeYuan(inputOrderDescriptionDTO.getPayAmount()));


        return Result.success(response);
    }


    @ApiOperation("支付-订单-JSAPI下单")
    @PostMapping("/payOrderByJsAPI")
    public Result<WxPayUnifiedOrderV3Result.JsapiResult> payOrderByJsAPI(@RequestBody @Valid InputOrderDescriptionVO inputOrderDescriptionVO) {

        UserDTO userDTO = ApiModuleRequestHolder.get();

        //下单
        WxPayUnifiedOrderV3Result.JsapiResult result = orderService.addOrderByJSAPI(userDTO.getId(), inputOrderDescriptionVO);


        return Result.success(result);
    }
}
