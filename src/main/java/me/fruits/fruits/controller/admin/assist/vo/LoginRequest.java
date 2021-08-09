package me.fruits.fruits.controller.admin.assist.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @ApiModelProperty(value = "登录的账号",name = "username",required = true)
    @NotNull
    @Length(max = 2)
    private String username;

    @ApiModelProperty(value = "登录的密码",name = "password",required = true)
    @NotNull
    private String password;
}
