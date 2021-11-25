package me.fruits.fruits.service.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("admin管理后台，用户统一绑定微信的通用信息")
public class UserWeChatForAdminDTO {

    @ApiModelProperty(name = "id", value = "唯一标识")
    private Long id;

    @ApiModelProperty(name = "微信的载体,小程序，公众号等")
    private String carrier;

    @ApiModelProperty(name = "openId")
    private String openId;
}
