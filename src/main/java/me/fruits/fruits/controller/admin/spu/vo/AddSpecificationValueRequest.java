package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AddSpecificationValueRequest {

    @ApiModelProperty(name = "specificationId",value = "规格id",required = true)
    @NotNull
    private Long specificationId;

    @ApiModelProperty(name = "value",value = "规格值",required = true)
    @NotEmpty
    private String value;

    @ApiModelProperty(name = "money",value = "金额，单位元")
    @Pattern(regexp = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$",message = "金额格式不对")
    private String money;
}
