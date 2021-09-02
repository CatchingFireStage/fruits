package me.fruits.fruits.controller.api.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.api.login.vo.WeChatMiniProgramCodeRequest;
import me.fruits.fruits.controller.api.login.vo.WeChatMiniProgramPhoneRequest;
import me.fruits.fruits.service.user.UserWeChatApiModuleService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/login")
@RestController(value = "apiWeChatController")
@Api(tags = "登录-微信")
@Validated
public class WeChatController {

    @Autowired
    private UserWeChatApiModuleService userWeChatApiModuleService;

    @PostMapping("/weChatMiniProgramCode")
    @ApiOperation(value = "微信小程序-code换取用户id")
    public Result<Long> miniProgramCode(@RequestBody @Valid WeChatMiniProgramCodeRequest weChatMiniProgramCodeRequest) {


        //todo: 微信小程序code登录，获取用户的openId和sessionKey
        weChatMiniProgramCodeRequest.getCode();


        String openId = "11111";
        String sessionKey = "22222";


        return Result.success(userWeChatApiModuleService.loginMiniProgram(openId, sessionKey));
    }

    @PostMapping("weChatMiniProgramPhone")
    @ApiOperation(value = "微信小程序-手机号登录")
    public Result<String> miniProgramPhone(@RequestBody @Valid WeChatMiniProgramPhoneRequest weChatMiniProgramPhoneRequest) {


        //todo: 微信解密数据，获取用户的手机号

        String phone = "17602203718";


        return Result.success(
                userWeChatApiModuleService.loginMiniProgram(weChatMiniProgramPhoneRequest.getId(), phone)
        );
    }
}
