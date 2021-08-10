package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddSpuRequest {

    @ApiModelProperty(value = "商品名字", name = "name", required = true)
    @NotNull(message = "商品名字不能为空")
    private String name;

    @ApiModelProperty(value = "商品分类id", name = "categoryId", required = true)
    @NotNull(message = "商品分类不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "是否有货", name = "isInventory", required = true)
    @NotNull(message = "是否有货； 0没有货：1有货")
    @Size(min = 0, max = 1, message = "0没有货，1有货")
    private Integer isInventory;
}
