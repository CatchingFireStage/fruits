package me.fruits.fruits.service.pay.refund;


import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.RefundMapper;
import me.fruits.fruits.mapper.enums.pay.PayStateEnum;
import me.fruits.fruits.mapper.enums.pay.refund.RefundStateEnum;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.mapper.po.Refund;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RefundService {


    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private PayService payService;

    /**
     * 微信订单申请退款
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

        refundMapper.insert(refund);

        //更新支付表记录的状态
        if (payService.updateStateToRefund(pay.getId()) <= 0) {
            throw new FruitsRuntimeException("支付表的状态更新失败");
        }

        //todo 微信退款
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
