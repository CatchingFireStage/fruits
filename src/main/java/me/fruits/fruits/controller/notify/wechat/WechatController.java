package me.fruits.fruits.controller.notify.wechat;


import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.service.pay.refund.RefundService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RefundService refundService;


    @PostMapping("/pay")
    @ApiOperation("微信支付-支付结果通知接口")
    public Map<String, String> pay(@RequestBody String jsonData, @RequestHeader HttpHeaders headers) throws WxPayException {

        log.info("微信推送的数据:{}", jsonData);
        log.info("微信推送的请求头:{}", headers);

        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setNonce(headers.get("wechatpay-nonce").get(0));
        signatureHeader.setSerial(headers.get("wechatpay-serial").get(0));
        signatureHeader.setSignature(headers.get("wechatpay-signature").get(0));
        signatureHeader.setTimeStamp(headers.get("wechatpay-timestamp").get(0));

        //微信支付的回调数据
        WxPayOrderNotifyV3Result wxPayOrderNotifyV3Result = wxPayService.parseOrderNotifyV3Result(jsonData, signatureHeader);

        log.info("微信推送的数据，解密之后:{}", wxPayOrderNotifyV3Result);

        //更新支付订单
        payService.updateStateToSuccess(wxPayOrderNotifyV3Result.getResult().getTransactionId(), wxPayOrderNotifyV3Result.getResult().getOutTradeNo());

        Map<String, String> response = new HashMap<>();

        log.info("感谢上帝，微信支付成功了");

        response.put("code", "SUCCESS");
        response.put("message", "成功");

        return response;
    }

    @PostMapping("/refund")
    @ApiOperation("微信支付-退款结果通知接口")
    public Map<String, String> refund(@RequestBody String jsonData, @RequestHeader HttpHeaders headers) throws WxPayException {

        log.info("微信推送的退款数据:{}", jsonData);
        log.info("微信推送的退款请求头:{}", headers);

        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setNonce(headers.get("wechatpay-nonce").get(0));
        signatureHeader.setSerial(headers.get("wechatpay-serial").get(0));
        signatureHeader.setSignature(headers.get("wechatpay-signature").get(0));
        signatureHeader.setTimeStamp(headers.get("wechatpay-timestamp").get(0));

        //退款结果
        WxPayRefundNotifyV3Result wxPayRefundNotifyV3Result = wxPayService.parseRefundNotifyV3Result(jsonData, signatureHeader);

        //业务逻辑
        String refundStatus = wxPayRefundNotifyV3Result.getResult().getRefundStatus();

        //商户的退款订单号
        long outRefundNo = Long.parseLong(wxPayRefundNotifyV3Result.getResult().getOutRefundNo());
        //微信的退款订单号
        String refundId = wxPayRefundNotifyV3Result.getResult().getRefundId();


        switch (refundStatus) {
            case "CLOSE":
                //退款订单切换到close状态
                if (refundService.updateStateToCloseAndRefundId(outRefundNo, refundId)) {
                    throw new FruitsRuntimeException("退款订单切换到close失败:outRefundNo:" + outRefundNo);
                }
                break;
            case "ABNORMAL":
                //退款订单切换到abnormal状态
                if (refundService.updateStateToAbnormalAndRefundId(outRefundNo, refundId)) {
                    throw new FruitsRuntimeException("退款订单切换到abnormal失败:outRefundNo:" + outRefundNo);
                }
                break;
            case "SUCCESS":
                //退款订单，退款成功
                //防止微信重复通知,通过更新是否大于0判断是否已经处理过业务
                if (refundService.updateStateToSuccessAndRefundId(outRefundNo, refundId)) {
                    throw new FruitsRuntimeException("退款订单切换到success失败:outRefundNo:" + outRefundNo + "已经处理业务了，微信可以不需要再发送了");
                }
                break;
            default:
                throw new FruitsRuntimeException("未知的微信退款状态：" + refundStatus);
        }


        Map<String, String> response = new HashMap<>();

        response.put("code", "SUCCESS");
        response.put("message", "成功");

        return response;
    }
}
