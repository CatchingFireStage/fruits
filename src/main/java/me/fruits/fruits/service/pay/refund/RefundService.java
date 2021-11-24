package me.fruits.fruits.service.pay.refund;


import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.Data;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RefundService {


    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private PayService payService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private SnowFlake snowFlake;

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
        Pay pay = checkRefundPremise(payId, amount);

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


        refundMapper.insert(refund);

        //更新支付表记录的状态
        if (payService.updateStateToRefund(pay.getId()) <= 0) {
            throw new FruitsRuntimeException("支付表的状态更新失败");
        }


        try {

            //微信退款申请
            WxPayRefundV3Request refundV3Request = new WxPayRefundV3Request();

            //原支付交易对应的商户订单号
            refundV3Request.setOutTradeNo(String.valueOf(pay.getOutTradeNo().longValue()));

            //退款单号
            refundV3Request.setOutRefundNo(String.valueOf(outRefundNo));
            //退款原因
            refundV3Request.setReason(reason);

            //退款回调地址,可以不填，在微信的商户平台生设置也可以
            refundV3Request.setNotifyUrl("https://www.baidu.com");

            //退款金额
            WxPayRefundV3Request.Amount refundAmount = new WxPayRefundV3Request.Amount();

            //本次退款金额
            refundAmount.setRefund(amount);
            //原订单金额
            refundAmount.setTotal(pay.getAmount());
            refundAmount.setCurrency("CNY");

            refundV3Request.setAmount(refundAmount);

            //微信申请退款
            wxPayService.refundV3(refundV3Request);


            //更新订单已经退款的金额
            if (payService.accumulationRefundAmount(payId, amount) <= 0) {
                throw new FruitsRuntimeException("更新refundAmount失败");
            }

        } catch (WxPayException wxPayException) {
            throw new FruitsRuntimeException("微信申请退款接口请求失败");
        }
    }

    /**
     * 检查退款的前提条件
     *
     * @param payId  支付id
     * @param amount 本次退矿金额，单位分
     */
    private Pay checkRefundPremise(long payId, int amount) {

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

        return pay;

    }
}
