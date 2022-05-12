package me.fruits.fruits.service.pay.refund;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.RefundMapper;
import me.fruits.fruits.mapper.enums.pay.PayStateEnum;
import me.fruits.fruits.mapper.enums.pay.refund.RefundStateEnum;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.mapper.po.Refund;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RefundService extends ServiceImpl<RefundMapper, Refund> {


    @Autowired
    private PayService payService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 微信退款接受地址
     */
    @Value("${wx.pay.refund-notify-url}")
    private String refundNotifyUrl;


    /**
     * 支付id获取退款记录
     *
     * @param payId 支付id
     */
    public List<Refund> getRefunds(long payId) {
        return lambdaQuery().eq(Refund::getPayId, payId).list();
    }

    /**
     * 获取可以重新申请退款的退款订单
     * <p>
     * 状态是退款异常或者是退款关闭的，可以重新按照原来的记录重新申请退款
     *
     * @param id 订单id
     */
    private Refund getRefundByReiterate(long id) {

        LambdaQueryChainWrapper<Refund> refundQueryWrapper = lambdaQuery();

        refundQueryWrapper.eq(Refund::getId, id);
        refundQueryWrapper.nested(consumer -> {
            consumer.eq(Refund::getState, RefundStateEnum.ABNORMAL.getValue()).or()
                    .eq(Refund::getState, RefundStateEnum.CLOSE.getValue());
        });

        return refundQueryWrapper.one();
    }

    /**
     * 微信订单
     * <p>
     * 第一次 申请退款
     * <p>
     * <p>
     * 微信注意：
     * 1、交易时间超过一年的订单无法提交退款
     * <p>
     * 2、(重点)微信支付退款支持单笔交易分多次退款（不超50次），多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。
     * 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
     * <p>
     * 3、错误或无效请求频率限制：6qps，即每秒钟异常或错误的退款申请请求不超过6次
     * <p>
     * 4、每个支付订单的部分退款次数不能超过50次
     * <p>
     * 5、如果同一个用户有多笔退款，建议分不同批次进行退款，避免并发退款导致退款失败
     * <p>
     * 6、申请退款接口的返回仅代表业务的受理情况，具体退款是否成功，需要通过退款查询接口获取结果
     * <p>
     * 7、一个月之前的订单申请退款频率限制为：5000/min
     *
     * @param payId  支付id
     * @param reason 退款信息
     * @param amount 本次退款金额，单位分
     */
    @Transactional
    public void addRefund(long payId, String reason, int amount) {


        //退款的前提条件检查
        Pay pay = payService.getPay(payId);

        if (pay == null) {
            throw new FruitsRuntimeException("找不到支付记录");
        }

        if (!PayStateEnum.SUCCESS.getValue().equals(pay.getState()) && !PayStateEnum.REFUND.getValue().equals(pay.getState())) {
            throw new FruitsRuntimeException("申请退款前提是，订单已经支付完成，或者是订单已经部分退款");
        }

        //下次退款总金额 = 原来的退款金额 + 本次退款金额
        int nextRefundAmountTotal = pay.getRefundAmount() + amount;

        if (nextRefundAmountTotal > pay.getAmount()) {
            throw new FruitsRuntimeException("退款总金额不能大于订单实际支付的金额");
        }


        //申请退款
        Refund refund = new Refund();
        refund.setPayId(pay.getId());
        refund.setReason(reason);
        refund.setAmount(amount);
        refund.setState(RefundStateEnum.REFUND.getValue());
        refund.setCreateTime(LocalDateTime.now());
        //商户退款订单号
        long outRefundNo = snowFlake.nextId();

        refund.setOutRefundNo(outRefundNo);


        save(refund);

        //更新支付表记录的状态到进入退款
        payService.updateStateToRefund(pay.getId());

        //微信接口退款申请
        weChatRefund(pay.getOutTradeNo(), outRefundNo, reason, amount, pay.getAmount());


        //更新订单已经退款的金额
        payService.accumulationRefundAmount(payId, amount);

    }


    /**
     * 微信退退款申请
     *
     * @param outTradeNo  商户原支付订单号
     * @param outRefundNo 商户退款订单号
     * @param reason      退款原因
     * @param amount      本次退款金额 单位分
     * @param payAmount   原支付订单总金额 单位分
     */
    private void weChatRefund(long outTradeNo, long outRefundNo, String reason, int amount, int payAmount) {
        try {

            //微信退款申请
            WxPayRefundV3Request refundV3Request = new WxPayRefundV3Request();

            //原支付交易对应的商户订单号
            refundV3Request.setOutTradeNo(String.valueOf(outTradeNo));

            //退款单号
            refundV3Request.setOutRefundNo(String.valueOf(outRefundNo));
            //退款原因
            refundV3Request.setReason(reason);

            //退款回调地址,可以不填，在微信的商户平台生设置也可以
            refundV3Request.setNotifyUrl(refundNotifyUrl);

            //退款金额
            WxPayRefundV3Request.Amount refundAmount = new WxPayRefundV3Request.Amount();

            //本次退款金额
            refundAmount.setRefund(amount);
            //原订单金额
            refundAmount.setTotal(payAmount);
            refundAmount.setCurrency("CNY");

            refundV3Request.setAmount(refundAmount);

            //微信申请退款
            wxPayService.refundV3(refundV3Request);

        } catch (WxPayException wxPayException) {
            throw new FruitsRuntimeException("微信申请退款接口请求失败");
        }
    }


    /**
     * 重新申请退款
     * 当退款状态是 close 或者是 abnormal的时候，说明退款失败了，需要基于原来的数据，重新申请退款
     *
     * @param id
     */
    @Transactional
    public void reiterateRefund(long id) {
        //原退款订单
        Refund refundByReiterate = getRefundByReiterate(id);

        //获取原支付订单
        Pay pay = payService.getPay(refundByReiterate.getPayId());

        //重新退款
        weChatRefund(pay.getOutTradeNo(), refundByReiterate.getOutRefundNo(), refundByReiterate.getReason(), refundByReiterate.getAmount(), pay.getAmount());
    }

    /**
     * 更新退款状态到关闭
     *
     * @param outRefundNo 商户退款订单号
     * @param refundId    微信支付退款单号
     */
    public boolean updateStateToCloseAndRefundId(long outRefundNo, String refundId) {

        LambdaUpdateChainWrapper<Refund> updateWrapper = lambdaUpdate();

        updateWrapper.eq(Refund::getOutRefundNo, outRefundNo);
        updateWrapper.eq(Refund::getState, RefundStateEnum.REFUND.getLabel());


        updateWrapper.set(Refund::getState, RefundStateEnum.CLOSE.getValue());
        updateWrapper.set(Refund::getRefundId, refundId);


        return updateWrapper.update();

    }


    /**
     * 更新退款状态到异常, 异常订单
     */
    public boolean updateStateToAbnormalAndRefundId(long outRefundNo, String refundId) {

        LambdaUpdateChainWrapper<Refund> updateWrapper = lambdaUpdate();

        updateWrapper.eq(Refund::getOutRefundNo, outRefundNo);
        updateWrapper.eq(Refund::getState, RefundStateEnum.REFUND.getLabel());


        updateWrapper.set(Refund::getState, RefundStateEnum.ABNORMAL.getValue());
        updateWrapper.set(Refund::getRefundId, refundId);


        return updateWrapper.update();
    }


    /**
     * 更新退款状态到退款成功
     *
     * @param outRefundNo
     * @param refundId
     */
    public boolean updateStateToSuccessAndRefundId(long outRefundNo, String refundId) {

        LambdaUpdateChainWrapper<Refund> updateWrapper = lambdaUpdate();

        updateWrapper.eq(Refund::getOutRefundNo, outRefundNo);
        //防止微信重复通知,通过更新是否大于0判断是否已经处理过业务
        updateWrapper.ne(Refund::getState, RefundStateEnum.SUCCESS.getValue());

        updateWrapper.set(Refund::getState, RefundStateEnum.SUCCESS.getValue());
        updateWrapper.set(Refund::getRefundId, refundId);

        return updateWrapper.update();
    }


}
