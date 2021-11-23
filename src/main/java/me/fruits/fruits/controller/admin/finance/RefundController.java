package me.fruits.fruits.controller.admin.finance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.finance.vo.AddRefundRequest;
import me.fruits.fruits.service.pay.refund.RefundService;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/finance")
@RestController(value = "AdminRefundController")
@Api(tags = "财务-退款")
@AdminLogin
@Validated
public class RefundController {


    @Autowired
    private RefundService refundService;

    @GetMapping(value = "/refund")
    @ApiOperation("退款-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "payid 支付表的id", example = "")
    })
    public Result<Object> refund(
            @RequestParam(required = false) String keyword,
            PageVo pageVo
    ) {

        return null;
    }


    @PostMapping(value = "/refund")
    @ApiOperation(value = "退款-申请")
    public Result<Object> refund(@Valid AddRefundRequest addRefundRequest) {

        return null;
    }
}
