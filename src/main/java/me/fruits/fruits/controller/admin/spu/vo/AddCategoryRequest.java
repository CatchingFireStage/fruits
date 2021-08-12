package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddCategoryRequest {

    @ApiModelProperty(value = "分类名", name = "name", required = true)
    @NotNull(message = "分类名不能为空")
    private String name;
}
