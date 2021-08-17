package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "specification_spu")
public class SpecificationSpu implements Serializable {
    private static final long serialVersionUID = -6674809205488515450L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "spuId")
    private Long spuId;

    @ApiModelProperty(value = "规格id")
    private Long specificationId;
}
