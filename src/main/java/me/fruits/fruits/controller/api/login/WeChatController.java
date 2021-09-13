package me.fruits.fruits.controller.api.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.api.login.vo.WeChatMiniProgramCodeRequest;
import me.fruits.fruits.controller.api.login.vo.WeChatMiniProgramPhoneRequest;
import me.fruits.fruits.service.api.LoginService;
import me.fruits.fruits.service.user.UserWeChatApiModuleService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/login")
@RestController(value = "apiWeChatController")
@Api(tags = "登录-微信")
@Validated
public class WeChatController {

    @Autowired
    private UserWeChatApiModuleService userWeChatApiModuleService;

    @Autowired
    private CacheManager cacheManager;


    @Autowired
    private LoginService loginService;

    @PostMapping("/weChatMiniProgramCode")
    @ApiOperation(value = "微信小程序-code换取用户id")
    public Result<Object> miniProgramCode(@RequestBody @Valid WeChatMiniProgramCodeRequest weChatMiniProgramCodeRequest) {


        //todo: 微信小程序code登录，获取用户的openId和sessionKey
        weChatMiniProgramCodeRequest.getCode();


        String openId = "11111";
        String sessionKey = "22222";


        Map<String, Object> response = new HashMap<>();
        response.put("accessTokenApi", "");
        response.put("openId", "");

        long userId = userWeChatApiModuleService.loginMiniProgram(openId, sessionKey);
        if (userId != 0) {
            //登录成功
            response.put("accessTokenApi", loginService.login(userId));
        } else {
            //登录不成功，引导用户获取他的手机号，注册并登录
            response.put("openId", openId);
        }

        //没有存在的用户,缓存用户信息，走微信的流程，引导用户获取手机号注册，然后注册用户
        Cache miniWeChatRegisterUser = cacheManager.getCache("miniWeChatRegisterUser");
        //保存用户的sessionKey
        miniWeChatRegisterUser.put(openId, sessionKey);

        return Result.success(response);
    }

    @PostMapping("weChatMiniProgramPhoneRegisterUser")
    @ApiOperation(value = "微信小程序-手机号注册")
    public Result<Object> weChatMiniProgramPhoneRegisterUser(@RequestBody @Valid WeChatMiniProgramPhoneRequest weChatMiniProgramPhoneRequest) {


        Cache miniWeChatRegisterUser = cacheManager.getCache("miniWeChatRegisterUser");
        String sessionKey = miniWeChatRegisterUser.get(weChatMiniProgramPhoneRequest.getOpenId(), String.class);

        //todo: 微信解密数据，获取用户的手机号
        String phone = "17602203718";

        Map<String, Object> response = new HashMap<>();
        response.put("accessTokenApi", userWeChatApiModuleService.registerUserByMiniProgram(weChatMiniProgramPhoneRequest.getOpenId(), phone, sessionKey));

        return Result.success(response);
    }
}
