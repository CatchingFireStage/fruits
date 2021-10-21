package me.fruits.fruits.controller.admin.pipeline;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pipeline")
@RestController(value = "AdminPayController")
@Api(tags = "流水-支付")
@AdminLogin
@Validated
public class PayController {

    @GetMapping(value = "/pay")
    @ApiOperation("支付-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "payid 支付表的id", example = "")
    })
    public Result<Object> refund(
            @RequestParam(required = false) String keyword,
            PageVo pageVo
    ) {

        return null;
    }
}
