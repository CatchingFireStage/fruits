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


    public void orderNative(long orderId, Orders orders) throws WxPayException {

        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();

        //商品描述
        wxPayUnifiedOrderV3Request.setDescription(orders.getDescription());

        //订单id设置
        wxPayUnifiedOrderV3Request.setOutTradeNo(String.format("%d", orderId));

//        //设置支付的超时时间,为2小时,微信默认的也是2小时
//        wxPayUnifiedOrderV3Request.setTimeExpire(LocalDateTime.now().plusHours(2).toString());

        //支付金额设置
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(orders.getPayMoney());
        wxPayUnifiedOrderV3Request.setAmount(amount);

        //设置通知地址
//        wxPayUnifiedOrderV3Request.setNotifyUrl();

        Object orderV3 = wxPayService.createOrderV3(TradeTypeEnum.NATIVE, wxPayUnifiedOrderV3Request);
    }
}
