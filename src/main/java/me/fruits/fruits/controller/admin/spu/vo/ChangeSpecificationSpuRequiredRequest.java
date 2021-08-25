package me.fruits.fruits.controller.admin.spu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("规格与spu之间的关联;改变它的必选属性")
public class ChangeSpecificationSpuRequiredRequest {

    @ApiModelProperty(name = "required", value = "是否为必选", required = true)
    @NotNull
    @Range(min = 0, max = 1, message = "0不是必选的，1必选")
    private Integer required;

}
