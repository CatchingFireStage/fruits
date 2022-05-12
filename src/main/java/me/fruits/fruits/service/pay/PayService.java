package me.fruits.fruits.service.pay;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.PayMapper;
import me.fruits.fruits.mapper.enums.pay.MerchantTransactionTypeEnum;
import me.fruits.fruits.mapper.enums.pay.PayStateEnum;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.service.order.OrderService;
import me.fruits.fruits.service.user.UserWeChatService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class PayService extends ServiceImpl<PayMapper, Pay> {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private UserWeChatService userWeChatService;

    @Autowired
    private SnowFlake snowFlake;


    @Autowired
    private OrderService orderService;


    public Pay getPay(long id) {
        return getById(id);
    }

    /**
     * 获取记录
     *
     * @param outTradeNo    系统订单号
     * @param transactionId 微信订单号
     */
    public Pay getPay(long outTradeNo, String transactionId) {

        return lambdaQuery().eq(Pay::getOutTradeNo, outTradeNo)
                .eq(Pay::getTransactionId, transactionId).one();

    }

    public Pay getPay(long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum) {

        return lambdaQuery().eq(Pay::getMerchantTransactionId, merchantTransactionId)
                .eq(Pay::getMerchantTransactionType, merchantTransactionTypeEnum.getValue())
                .one();

    }

    /**
     * 通过商户的订单号获取记录
     */
    public List<Pay> getPays(Set<Long> outTradeNo) {

        return lambdaQuery().in(Pay::getOutTradeNo, outTradeNo).list();
    }

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
    public WxPayUnifiedOrderV3Result.JsapiResult orderJSPAPI(long userId, long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum, Orders orders) throws WxPayException {


        //创建微信的支付订单

        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();

        //微信支付载体的appId
        wxPayUnifiedOrderV3Request.setAppid(wxPayService.getConfig().getAppId());
        //直连商户的商户号（打钱给哪个号）
        wxPayUnifiedOrderV3Request.setMchid(wxPayService.getConfig().getMchId());


        //订单号
        long outTradeNo = snowFlake.nextId();
        wxPayUnifiedOrderV3Request.setOutTradeNo(String.format("%d", outTradeNo));

        //商品描述
        wxPayUnifiedOrderV3Request.setDescription("fruits订单支付");

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


        WxPayUnifiedOrderV3Result.JsapiResult orderV3 = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request);
        log.info("微信jsapi结果JsapiResult:{}", orderV3);

        //创建支付订单

        addPay(merchantTransactionId, merchantTransactionTypeEnum, outTradeNo, orders);

        return orderV3;
    }


    /**
     * 创建支付订单
     */
    private void addPay(long merchantTransactionId, MerchantTransactionTypeEnum merchantTransactionTypeEnum, long outTradeNo, Orders orders) {

        Pay pay = new Pay();
        pay.setMerchantTransactionId(merchantTransactionId);
        pay.setMerchantTransactionType(merchantTransactionTypeEnum.getValue());
        pay.setAmount(orders.getPayMoney());
        pay.setCreateTime(LocalDateTime.now());
        pay.setState(PayStateEnum.ORDER.getValue());
        pay.setOutTradeNo(outTradeNo);

        save(pay);
    }


    /**
     * 累加本次退款金额
     *
     * @param id     支付id
     * @param amount 本次退款的金额
     */
    public int accumulationRefundAmount(long id, int amount) {
        return this.baseMapper.accumulationRefundAmount(id, amount);
    }

    /**
     * 更新订单状态到 退款
     *
     * @param id 订单id
     */
    public void updateStateToRefund(long id) {

        LambdaUpdateChainWrapper<Pay> updateWrapper = lambdaUpdate();

        updateWrapper.eq(Pay::getId, id);

        //已支付或者部分退款的，都可以切换到退款状态
        updateWrapper.nested(nested -> {

            nested.eq(Pay::getState, PayStateEnum.SUCCESS.getValue()).or().eq(Pay::getState, PayStateEnum.REFUND.getValue());

        });

        updateWrapper.set(Pay::getState, PayStateEnum.REFUND.getValue());

        updateWrapper.update();
    }

    /**
     * 支付回调
     *
     * @param outTradeNo    fruits系统订单号
     * @param transactionId 微信的支付的订单号
     */
    @Transactional
    public void updateStateToSuccess(String transactionId, String outTradeNo) {

        LambdaUpdateChainWrapper<Pay> updateWrapper = lambdaUpdate();

        long out_trade_no = Long.parseLong(outTradeNo);

        updateWrapper.eq(Pay::getOutTradeNo, out_trade_no);
        updateWrapper.eq(Pay::getState, PayStateEnum.ORDER.getValue());

        //更新状态
        updateWrapper.set(Pay::getState, PayStateEnum.SUCCESS.getValue());
        //设置微信的订单id
        updateWrapper.set(Pay::getTransactionId, transactionId);

        if (!updateWrapper.update()) {
            //更新失败,让微信再次通知
            String format = String.format("微信的transactionId:%s,系统订单号outTradeNo:%s从0到1的状态更新失败", transactionId, outTradeNo);
            log.error(format);
            throw new FruitsRuntimeException("订单");
        }


        //获取支付订单
        Pay pay = getPay(out_trade_no, transactionId);


        if (pay.getMerchantTransactionType().equals(MerchantTransactionTypeEnum.ORDER.getValue())) {

            //订单类型的

            //更新订单状态
            orderService.updateStatusToPay(pay.getMerchantTransactionId());
        }


    }
}
