package me.fruits.fruits.service.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "admin管理后台，用户统一通用信息")
public class UserForAdminDTO {

    @ApiModelProperty(name = "id", value = "用户唯一标识")
    private Long id;

    @ApiModelProperty(name = "phone", value = "用户手机号")
    private String phone;

    @ApiModelProperty(name = "weChatBind", value = "绑定微信的载体有哪些")
    private List<UserWeChatForAdminDTO> weChatBind;
}
