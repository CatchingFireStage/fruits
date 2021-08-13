package me.fruits.fruits.service.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public abstract class ResourceVO {

    @ApiModelProperty("文件类型")
    protected String fileType;

    @ApiModelProperty("相对路径")
    protected String url;

    @ApiModelProperty("完整路径")
    protected String fullUrl;

    @ApiModelProperty("文件大小")
    protected Integer size;
}
