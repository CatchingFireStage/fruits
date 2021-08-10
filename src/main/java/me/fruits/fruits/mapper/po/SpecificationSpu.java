package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpecificationSpu implements Serializable {
    private static final long serialVersionUID = -6674809205488515450L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty("唯一标识")
    private Long id;

    @ApiModelProperty("spuId")
    private Long spuId;

    @ApiModelProperty("规格id")
    private Long specificationId;
}
