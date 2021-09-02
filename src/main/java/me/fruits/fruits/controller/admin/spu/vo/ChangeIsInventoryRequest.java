package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class ChangeIsInventoryRequest {

    @ApiModelProperty(value = "是否有货", name = "isInventory", required = true)
    @NotNull(message = "是否有货； 0没有货：1有货")
    @Range(min = 0, max = 1, message = "0没有货，1有货")
    private Integer isInventory;
}
