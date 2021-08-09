package me.fruits.fruits.controller.admin.assist.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @ApiModelProperty(value = "登录的账号",name = "username",required = true)
    @NotNull(message = "username不能为空")
    private String username;

    @ApiModelProperty(value = "登录的密码",name = "password",required = true)
    @NotNull(message = "password不能为空")
    private String password;
}
