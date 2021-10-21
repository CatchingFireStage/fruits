package me.fruits.fruits.service.pay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import me.fruits.fruits.mapper.po.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.stereotype.Service;


@Service
public class PayService {

    @Autowired
    private WxPayService wxPayService;

    /**
     * JSP API订单统一支付
     * @param merchantTransactionId 订单id
     * @param merchantTransactionTypeEnum 交易类型
     * @param orders 订单
     * @throws WxPayException
     */
    public void orderJSPAPI(long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum, Orders orders) throws WxPayException {

        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();

        //商品描述
        wxPayUnifiedOrderV3Request.setDescription(orders.getDescription());

        //订单id设置
        wxPayUnifiedOrderV3Request.setOutTradeNo(String.format("%d", merchantTransactionId));


        //支付金额设置
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(orders.getPayMoney());
        wxPayUnifiedOrderV3Request.setAmount(amount);


        Object orderV3 = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request);
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
