package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AddSpuRequest {

    @ApiModelProperty(value = "商品名", name = "name", required = true, example = "商品名字")
    @NotNull(message = "商品名字不能为空")
    private String name;

    @ApiModelProperty(value = "商品分类id", name = "categoryId", required = true, example = "0")
    @NotNull(message = "商品分类不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "金额,单位元",name = "money",required = true,example = "1.00")
    @NotNull
    @Pattern(regexp = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$",message = "金额格式不对")
    private String money;

    @ApiModelProperty(value = "0没有货，1有货", name = "isInventory", required = true, example = "0")
    @NotNull
    @Range(min = 0, max = 1, message = "0没有货，1有货")
    private Integer isInventory;
}
