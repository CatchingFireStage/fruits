package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("规格与spu之间的关联")
public class AddSpecificationSpuRequest {

    @ApiModelProperty(name = "spuId",value = "关联的spuId",required = true)
    @NotNull
    private Long spuId;

    @ApiModelProperty(name = "specificationId",value = "关联的规格id",required = true)
    @NotNull
    private Long specificationId;
}
