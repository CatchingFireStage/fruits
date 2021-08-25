package me.fruits.fruits.service.spu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.fruits.fruits.service.upload.ImageVO;

import java.util.List;

@Data
@ApiModel("SpuDTO")
public class SpuDTO {

    @ApiModelProperty(name = "id", value = "spu唯一标识")
    private Long id;

    @ApiModelProperty(name = "name", value = "商品名")
    private String name;


    @ApiModelProperty(name = "category", value = "商品分类")
    private SpuCategoryDTO category;

    @ApiModelProperty(name = "isInventory", value = "是否有货")
    private Integer isInventory;

    @ApiModelProperty(name = "imag", value = "商品图片")
    private ImageVO image;


    @ApiModelProperty(name = "specificationSPUs",value = "规格列表")
    private List<SpecificationSpuDTO> specificationSPUs;

}
