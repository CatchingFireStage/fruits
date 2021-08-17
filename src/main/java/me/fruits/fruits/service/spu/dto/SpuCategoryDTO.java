package me.fruits.fruits.service.spu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SpuCategoryDTO {

    @ApiModelProperty(name = "id", value = "spu唯一标识")
    private Long id;

    @ApiModelProperty(name = "name", value = "商品名")
    private String name;
}
