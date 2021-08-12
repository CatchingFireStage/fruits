package me.fruits.fruits.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分页")
public class PageVo {

    @ApiModelProperty(name = "p" ,example = "1",notes = "第几页")
    private Integer p;

    @ApiModelProperty(name = "pageSize",example = "50",notes = "每页多少条")
    private Integer pageSize;
}
