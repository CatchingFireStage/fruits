package me.fruits.fruits.service.api;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 前台用户登录
 */
@Data
@ApiModel("前台登录用户")
public class UserDTO {
    @ApiModelProperty(value = "user表的id，用户id")
    private Long id;
}
