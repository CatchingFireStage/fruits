package me.fruits.fruits.service.pay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.service.user.UserWeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.stereotype.Service;


@Service
public class PayService {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private UserWeChatService userWeChatService;

    /**
     * JSP API订单统一支付
     *
     * @param userId                      系统的用户id
     * @param merchantTransactionId       订单id
     * @param merchantTransactionTypeEnum 交易类型
     * @param orders                      订单
     * @throws WxPayException
     */
    public Object orderJSPAPI(long userId, long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum, Orders orders) throws WxPayException {


        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();

        //微信支付载体的appId
        wxPayUnifiedOrderV3Request.setAppid(wxPayService.getConfig().getAppId());
        //直连商户的商户号（打钱给哪个号）
        wxPayUnifiedOrderV3Request.setMchid(wxPayService.getConfig().getMchId());


        //订单id设置
        wxPayUnifiedOrderV3Request.setOutTradeNo(String.format("%d", merchantTransactionId));

        //商品描述
        wxPayUnifiedOrderV3Request.setDescription(orders.getDescription());

        //支付成功后的回调地址
        wxPayUnifiedOrderV3Request.setNotifyUrl(wxPayService.getConfig().getPayScoreNotifyUrl());

        //支付金额设置
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(orders.getPayMoney());
        wxPayUnifiedOrderV3Request.setAmount(amount);

        //支付者
        UserWeChat userWeChatByMiniProgramFormUserId = userWeChatService.getUserWeChatByMiniProgramFormUserId(userId);
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(userWeChatByMiniProgramFormUserId.getOpenId());
        wxPayUnifiedOrderV3Request.setPayer(payer);


        Object orderV3 = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request);
        System.out.println(orderV3);

        return orderV3;
    }


    //支付的订单类型
    public enum MerchantTransactionTypeEnum {

        ORDER(1, "订单"),
        RECHARGE(2, "充值");

        private int value;

        private String label;

        MerchantTransactionTypeEnum(int value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
