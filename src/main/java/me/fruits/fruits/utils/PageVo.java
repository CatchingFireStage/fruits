package me.fruits.fruits.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分页vo参数接收者")
public class PageVo {

    @ApiModelProperty(value = "第几页")
    private Integer p;

    @ApiModelProperty(value = "每页多少条")
    private Integer pageSize;
}
