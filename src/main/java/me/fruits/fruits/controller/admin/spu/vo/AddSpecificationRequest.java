package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddSpecificationRequest {
    @ApiModelProperty(value = "规格名", name = "name", required = true, example = "")
    @NotNull(message = "规格名不能为空")
    private String name;

}
