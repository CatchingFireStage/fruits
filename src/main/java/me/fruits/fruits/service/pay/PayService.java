package me.fruits.fruits.service.pay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import me.fruits.fruits.mapper.PayMapper;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.service.user.UserWeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PayService {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private UserWeChatService userWeChatService;

    @Autowired
    private PayMapper payMapper;

    /**
     * JSP API订单统一支付
     *
     * @param userId                      系统的用户id
     * @param merchantTransactionId       订单id
     * @param merchantTransactionTypeEnum 交易类型
     * @param orders                      订单
     * @return 返回小程序需要的支付Id
     * @throws WxPayException
     */
    public String orderJSPAPI(long userId, long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum, Orders orders) throws WxPayException {


        //创建微信的支付订单

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

        //创建支付订单

        addPay(merchantTransactionId, merchantTransactionTypeEnum, orders, ((WxPayUnifiedOrderV3Result) orderV3).getPrepayId());

        return ((WxPayUnifiedOrderV3Result) orderV3).getPrepayId();
    }


    /**
     * 创建支付订单
     */
    private void addPay(long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum, Orders orders, String prepayId) {

        Pay pay = new Pay();
        pay.setMerchantTransactionId(merchantTransactionId);
        pay.setMerchantTransactionType(merchantTransactionTypeEnum.getValue());
        pay.setDescription(orders.getDescription());
        pay.setAmount(orders.getPayMoney());
        pay.setCreateTime(LocalDateTime.now());
        pay.setState(0);
        pay.setTransactionId(prepayId);

        payMapper.insert(pay);
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

        public int getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }
    }
}
