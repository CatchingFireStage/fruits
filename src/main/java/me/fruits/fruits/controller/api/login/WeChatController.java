package me.fruits.fruits.controller.api.login;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
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
import javax.validation.constraints.NotNull;
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

    @Autowired
    private WxMaService weChatMiniApp;

    @PostMapping("/weChatMiniProgramCode")
    @ApiOperation(value = "微信小程序-code换取fruits系统用户Token")
    public Result<Object> miniProgramCode(@RequestBody @Valid WeChatMiniProgramCodeRequest weChatMiniProgramCodeRequest) throws WxErrorException {


        String code = weChatMiniProgramCodeRequest.getCode();


        WxMaJscode2SessionResult sessionInfo = weChatMiniApp.getUserService().getSessionInfo(code);

        if(sessionInfo == null){
            return Result.failed("微信接口无法获取用户数据");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("accessTokenApi", "");
        response.put("openId", "");

        long userId = userWeChatApiModuleService.isRegisterByMiniProgram(sessionInfo.getOpenid());

        if (userId != 0) {
            //登录成功
            response.put("accessTokenApi", loginService.login(userId));
        } else {
            //登录不成功，引导用户获取他的手机号，注册并登录
            response.put("openId", sessionInfo.getOpenid());
        }

        //没有存在的用户,缓存用户信息，走微信的流程，引导用户获取手机号注册，然后注册用户
        Cache miniWeChatRegisterUser = cacheManager.getCache("miniWeChatRegisterUser");

        //保存用户的sessionKey
        miniWeChatRegisterUser.put(sessionInfo.getOpenid(), sessionInfo);

        return Result.success(response);


    }

    @PostMapping("weChatMiniProgramPhoneRegisterUser")
    @ApiOperation(value = "微信小程序-手机号注册")
    public Result<Object> weChatMiniProgramPhoneRegisterUser(@RequestBody @Valid WeChatMiniProgramPhoneRequest weChatMiniProgramPhoneRequest) {


        //获取微信用户的session_key
        Cache miniWeChatRegisterUser = cacheManager.getCache("miniWeChatRegisterUser");

        WxMaJscode2SessionResult wxMaJscode2SessionResult = miniWeChatRegisterUser.get(weChatMiniProgramPhoneRequest.getOpenId(), WxMaJscode2SessionResult.class);
        if(wxMaJscode2SessionResult == null){
            return Result.failed("无法获取openId对应是缓存信息");
        }

        //数据解密
        WxMaPhoneNumberInfo phoneNoInfo = weChatMiniApp.getUserService().getPhoneNoInfo(wxMaJscode2SessionResult.getSessionKey(), weChatMiniProgramPhoneRequest.getEncryptedData(), weChatMiniProgramPhoneRequest.getIv());


        Map<String, Object> response = new HashMap<>();
        
        response.put("accessTokenApi", userWeChatApiModuleService.registerUserByMiniProgram(weChatMiniProgramPhoneRequest.getOpenId(), phoneNoInfo.getPhoneNumber()));

        return Result.success(response);
    }
}
