package me.fruits.fruits.controller.api.login.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("微信小程序手机号登录")
public class WeChatMiniProgramPhoneRequest {

    @ApiModelProperty(name = "id",value = "user_we_chat表的id")
    @NotNull
    private Long id;


    @ApiModelProperty(name = "encryptedData",value = "包括敏感数据在内的完整用户信息的加密数据")
    @NotNull
    private String encryptedData;

    @ApiModelProperty(name = "iv",value = "加密算法的初始向量")
    @NotNull
    private String iv;
}
