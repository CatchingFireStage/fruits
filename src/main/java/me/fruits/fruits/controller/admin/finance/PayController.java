package me.fruits.fruits.controller.admin.finance;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.mapper.enums.orders.OrderStateEnum;
import me.fruits.fruits.mapper.enums.pay.MerchantTransactionTypeEnum;
import me.fruits.fruits.mapper.enums.pay.PayStateEnum;
import me.fruits.fruits.mapper.enums.pay.refund.RefundStateEnum;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.mapper.po.Refund;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.service.pay.PayAdminModuleService;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.service.pay.refund.RefundService;
import me.fruits.fruits.service.user.UserAdminModuleService;
import me.fruits.fruits.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/finance")
@RestController(value = "AdminPayController")
@Api(tags = "财务-支付")
@AdminLogin
@Validated
public class PayController {


    @Autowired
    private PayAdminModuleService payAdminModuleService;

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserAdminModuleService userAdminModuleService;

    @Autowired
    private RefundService refundService;

    @GetMapping(value = "/pay")
    @ApiOperation("支付-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "out_trade_no pay表的商户订单号", example = ""),
            @ApiImplicitParam(name = "payStateEnum", value = "支付状态,ORDER:下单，SUCCESS已支付,FAIL支付失败,REFUND退款", example = "")
    })
    public Result<Object> pay(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "") String payStateEnum,
            PageVo pageVo
    ) {


        PayStateEnum payState;
        try {
            payState = PayStateEnum.valueOf(payStateEnum);
        } catch (IllegalArgumentException exception) {
            payState = null;
        }

        //获取数据
        IPage<Pay> pays = payAdminModuleService.getPays(keyword, MerchantTransactionTypeEnum.ORDER, payState, pageVo);

        if (pays.getRecords().size() == 0) {
            return Result.success(pays.getTotal(), pays.getPages(), new ArrayList<>());
        }


        List<Object> list = new ArrayList<>();

        pays.getRecords().forEach(pay -> {

            Map<String, Object> payItem = new HashMap<>();

            payItem.put("id", pay.getId());
            payItem.put("merchantTransactionId", pay.getMerchantTransactionId());
            payItem.put("merchantTransactionType", EnumUtils.changeToString(MerchantTransactionTypeEnum.class, pay.getMerchantTransactionType()));
            payItem.put("outTradeNo", pay.getOutTradeNo().toString());
            payItem.put("transactionId", pay.getTransactionId());
            payItem.put("createTime", DateFormatUtils.format(pay.getCreateTime()));
            payItem.put("amount", MoneyUtils.fenChangeYuan(pay.getAmount()));
            payItem.put("state", EnumUtils.changeToString(PayStateEnum.class, pay.getState()));
            payItem.put("refundAmount", MoneyUtils.fenChangeYuan(pay.getRefundAmount()));


            list.add(payItem);

        });

        return Result.success(pays.getTotal(), pays.getPages(), list);
    }


    @GetMapping(value = "/pay/{id}")
    @ApiOperation("支付-详情")
    public Result<Object> pay(@PathVariable long id) throws JsonProcessingException {

        //获取支付信息
        Pay pay = payService.getPay(id);


        Map<String, Object> response = new HashMap<>();

        //支付信息构建
        response.put("id", pay.getId());
        response.put("merchantTransactionId", pay.getMerchantTransactionId());
        response.put("merchantTransactionType", EnumUtils.changeToString(MerchantTransactionTypeEnum.class, pay.getMerchantTransactionType()));
        response.put("outTradeNo", pay.getOutTradeNo().toString());
        response.put("amount", MoneyUtils.fenChangeYuan(pay.getAmount()));
        response.put("createTime", DateFormatUtils.format(pay.getCreateTime()));
        response.put("refundAmount", MoneyUtils.fenChangeYuan(pay.getRefundAmount()));
        response.put("state", EnumUtils.changeToString(PayStateEnum.class, pay.getState()));
        response.put("transactionId", pay.getTransactionId());


        //商家的交易对象
        response.put("merchantTransactionObject", null);

        if (MerchantTransactionTypeEnum.ORDER.getValue().equals(pay.getMerchantTransactionType())) {
            //订单类型
            Orders order = orderService.getOrder(pay.getMerchantTransactionId());

            Map<String, Object> orderObject = new HashMap<>();

            orderObject.put("id", order.getId());
            orderObject.put("description", OrderService.inputOrderDescriptionDTOWrap(order.getDescription()));
            orderObject.put("state", EnumUtils.changeToString(OrderStateEnum.class, order.getState()));
            orderObject.put("createTime", DateFormatUtils.format(order.getCreateTime()));
            orderObject.put("user", userAdminModuleService.getUserForAdminDTO(order.getUserId()));

            response.put("merchantTransactionObject", orderObject);
        }


        //退款订单
        response.put("refund", new ArrayList<>());
        List<Refund> refunds = refundService.getRefunds(pay.getId());
        if (refunds != null) {

            List<Object> refundList = new ArrayList<>();

            refunds.forEach(refund -> {

                Map<String, Object> refundItem = new HashMap<>();

                refundItem.put("id", refund.getId());
                refundItem.put("reason", refund.getReason());
                refundItem.put("amount", MoneyUtils.fenChangeYuan(refund.getAmount()));
                refundItem.put("createTime", DateFormatUtils.format(refund.getCreateTime()));
                refundItem.put("outRefundNo", refund.getOutRefundNo().toString());
                refundItem.put("refundId", refund.getRefundId());
                refundItem.put("state", EnumUtils.changeToString(RefundStateEnum.class, refund.getState()));

                refundList.add(refundItem);
            });


            response.put("refund", refundList);
        }

        return Result.success(response);
    }


}
