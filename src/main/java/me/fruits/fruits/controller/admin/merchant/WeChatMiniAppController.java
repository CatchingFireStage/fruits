package me.fruits.fruits.controller.admin.merchant;


import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.utils.QRCodeUtil;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RequestMapping("/merchant")
@RestController(value = "AdminWeChatMiniAppController")
@Api(tags = "商家-微信小程序")
@AdminLogin
@Validated
public class WeChatMiniAppController {

    @Autowired
    private WxMaService weChatMiniApp;

    @ApiOperation("桌子微信小程序码")
    @GetMapping("/deskQrCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "desk", value = "桌子号", required = true)
    })
    public Result<String> deskQrCode(@RequestParam() String desk) throws WxErrorException {

        //scene格式为query：  ?desk=1&age=18
        String scene = UriComponentsBuilder.fromUriString("").queryParam("desk",desk).build().toString();

        WxMaQrcodeService qrcodeService = weChatMiniApp.getQrcodeService();

        byte[] qrCode = qrcodeService.createWxaCodeUnlimitBytes(scene, null, 430, true, null, false);


        return Result.success(QRCodeUtil.toBase64QRCode(qrCode));
    }
}
