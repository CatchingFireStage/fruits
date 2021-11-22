package me.fruits.fruits.controller.notify.wechat;


import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.service.pay.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/wechat")
@RestController(value = "NotifyWechatController")
@Api(tags = "微信")
@Slf4j
public class WechatController {


    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PayService payService;


    @PostMapping("/pay")
    @ApiOperation("微信支付结果通知接口")
    public Map<String, String> pay(@RequestBody String jsonData) throws WxPayException {

        log.info("微信推送的数据:{}", jsonData);

        log.info("wxPayService:{}",wxPayService);

        //微信支付的回调数据
        WxPayOrderNotifyV3Result wxPayOrderNotifyV3Result = wxPayService.parseOrderNotifyV3Result(jsonData, new SignatureHeader());

        log.info("微信推送的数据，解密之后:{}", wxPayOrderNotifyV3Result);

        //更新支付订单
        payService.updateStateToSuccess(wxPayOrderNotifyV3Result.getResult().getTransactionId(), wxPayOrderNotifyV3Result.getResult().getOutTradeNo());

        Map<String, String> response = new HashMap<>();

        response.put("code", "SUCCESS");
        response.put("message", "成功");

        return response;
    }
}
