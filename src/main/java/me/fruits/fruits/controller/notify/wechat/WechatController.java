package me.fruits.fruits.controller.notify.wechat;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wechat")
@RestController(value = "NotifyWechatController")
@Api(tags = "微信")
public class WechatController {


    @PostMapping("/pay")
    @ApiOperation("微信支付结果通知接口")
    public Result<String> pay(){
        return Result.success();
    }
}
