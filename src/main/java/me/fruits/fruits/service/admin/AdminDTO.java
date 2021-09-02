package me.fruits.fruits.service.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 管理员的登录
 */
@Data
@ApiModel("后台登录用户,目前没有admin体系")
public class AdminDTO {

    @ApiModelProperty(value = "admin表的id")
    private Long id;

    @ApiModelProperty(value = "管理员名字")
    private String name;
}
