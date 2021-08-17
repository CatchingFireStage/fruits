package me.fruits.fruits.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@ApiModel(value = "分页")
public class PageVo {

    @ApiModelProperty(name = "p", value = "第几页", example = "1", notes = "第几页")
    private Integer p = 1;

    @ApiModelProperty(name = "pageSize", value = "每页多少条", example = "50", notes = "每页多少条")
    private Integer pageSize = 50;
}
