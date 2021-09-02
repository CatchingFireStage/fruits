package me.fruits.fruits.controller.api.login.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("微信小程序code换取用户id")
public class WeChatMiniProgramCodeRequest {

    @ApiModelProperty(name = "code",value = "小程序登录的code")
    @NotNull(message = "code不能为空")
    private String code;

}
