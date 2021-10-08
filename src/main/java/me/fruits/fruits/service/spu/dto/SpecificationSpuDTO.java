package me.fruits.fruits.service.spu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("规格dto")
public class SpecificationSpuDTO {

    @ApiModelProperty(name = "id", value = "规格id")
    private Long id;

    @ApiModelProperty(name = "name", value = "规格名")
    private String name;

    @ApiModelProperty(name = "required",value = "规格和spu的中间表，是否必选规格")
    private Integer required;

    @ApiModelProperty(name = "specificationSpuId", value = "规格和spu的中间表唯一标识")
    private Long specificationSpuId;


    @ApiModelProperty(name = "values", value = "规格值")
    private List<SpecificationValueDTO> values;
}
