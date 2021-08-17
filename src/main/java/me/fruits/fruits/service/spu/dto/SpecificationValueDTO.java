package me.fruits.fruits.service.spu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("规格值dto")
public class SpecificationValueDTO {

    @ApiModelProperty(name = "id",value = "规格值唯一id")
    private Long id;

    @ApiModelProperty(name = "value",value = "规格值")
    private String value;
}
