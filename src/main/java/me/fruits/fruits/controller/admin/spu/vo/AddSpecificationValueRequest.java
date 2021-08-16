package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddSpecificationValueRequest {

    @ApiModelProperty(name = "specificationId",value = "规格id",required = true)
    @NotNull
    private Long specificationId;

    @ApiModelProperty(name = "value",value = "规格值",required = true)
    @NotEmpty
    private String value;
}
