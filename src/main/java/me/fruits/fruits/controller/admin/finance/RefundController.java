package me.fruits.fruits.controller.admin.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.finance.vo.AddRefundRequest;
import me.fruits.fruits.mapper.enums.pay.refund.RefundStateEnum;
import me.fruits.fruits.mapper.po.Refund;
import me.fruits.fruits.service.pay.refund.RefundAdminModuleService;
import me.fruits.fruits.service.pay.refund.RefundService;
import me.fruits.fruits.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/finance")
@RestController(value = "AdminRefundController")
@Api(tags = "财务-退款")
@AdminLogin
@Validated
public class RefundController {


    @Autowired
    private RefundService refundService;

    @Autowired
    private RefundAdminModuleService refundAdminModuleService;

    @GetMapping(value = "/refund")
    @ApiOperation("退款-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "payid 支付表的id", example = ""),
            @ApiImplicitParam(name = "state", value = "退款状态过滤,REFUND退款中,SUCCESS退款成功,ABNORMAL退款异常,CLOSE退款关闭", example = "")
    })
    public Result<Object> refund(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String state,
            PageVo pageVo
    ) {

        RefundStateEnum refundStateEnum;
        try {

            refundStateEnum = RefundStateEnum.valueOf(state);
        } catch (IllegalArgumentException e) {
            refundStateEnum = null;
        }

        //获取数据
        IPage<Refund> refunds = refundAdminModuleService.getRefunds(keyword, refundStateEnum, pageVo);

        if (refunds.getRecords().size() <= 0) {
            return Result.success(refunds.getTotal(), refunds.getPages(), new ArrayList<>());
        }

        //构建返回数据
        List<Object> list = new ArrayList<>();

        refunds.getRecords().forEach(refund -> {

            Map<String, Object> refundItem = new HashMap<>();

            refundItem.put("id", refund.getId());
            refundItem.put("amount", MoneyUtils.fenChangeYuan(refund.getAmount()));
            refundItem.put("outRefundNo", refund.getOutRefundNo());
            refundItem.put("createTime", DateFormatUtils.format(refund.getCreateTime()));
            refundItem.put("payId", refund.getPayId());
            refundItem.put("reason", refund.getReason());
            refundItem.put("state", EnumUtils.changeToString(RefundStateEnum.class, refund.getState()));


            list.add(refundItem);
        });

        return Result.success(refunds.getTotal(), refunds.getPages(), list);
    }


    @PostMapping(value = "/refund")
    @ApiOperation(value = "退款-申请")
    public Result<String> refund(@Valid AddRefundRequest addRefundRequest) {


        refundService.addRefund(addRefundRequest.getPayId(), addRefundRequest.getReason(), MoneyUtils.yuanChangeFen(addRefundRequest.getAmount()));

        return Result.success();
    }

    @PutMapping("/reiterateRefund/{id}")
    @ApiOperation(value = "退款-重新申请")
    public Result<String> reiterateRefund(@PathVariable long id) {

        refundService.reiterateRefund(id);

        return Result.success();
    }
}
