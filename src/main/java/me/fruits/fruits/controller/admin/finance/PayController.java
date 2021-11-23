package me.fruits.fruits.controller.admin.finance;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.mapper.enums.PayStateEnum;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.service.pay.PayAdminModuleService;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/finance")
@RestController(value = "AdminPayController")
@Api(tags = "财务-支付")
@AdminLogin
@Validated
public class PayController {


    @Autowired
    private PayAdminModuleService payAdminModuleService;

    @GetMapping(value = "/pay")
    @ApiOperation("支付-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "out_trade_no pay表的商户订单号", example = ""),
            @ApiImplicitParam(name = "payStateEnum", value = "支付状态,ORDER:下单，SUCCESS已支付,FAIL支付失败,REFUND退款", example = "")
    })
    public Result<Object> pay(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String payStateEnum,
            PageVo pageVo
    ) {

        PayStateEnum payState;
        try {

            payState = PayStateEnum.valueOf(payStateEnum);
        } catch (IllegalArgumentException exception) {
            payState = null;
        }

        //获取数据
        IPage<Pay> pays = payAdminModuleService.getPays(keyword, PayService.MerchantTransactionTypeEnum.ORDER, payState, pageVo);


        

        return null;
    }
}
