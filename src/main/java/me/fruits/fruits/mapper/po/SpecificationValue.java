package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@TableName(value = "specification_value")
public class SpecificationValue implements Serializable {
    private static final long serialVersionUID = 2441296441524352342L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "规格id")
    private Long specificationId;

    @ApiModelProperty(value = "规格值")
    private String value;

    @ApiModelProperty(value = "金额,单位分")
    @Min(0)
    private Integer money;
}
