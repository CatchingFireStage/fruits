package me.fruits.fruits.service.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImageVO extends ResourceVO {
    @ApiModelProperty("宽度")
    private Integer width;

    @ApiModelProperty("高度")
    private Integer height;

    @ApiModelProperty("缩略图地址")
    private String thumbnail;

    public ImageVO(){}
}
