package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "specification")
public class Specification implements Serializable {

    private static final long serialVersionUID = -7716047697982578798L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("唯一标识")
    private Long id;

    @ApiModelProperty("规格名")
    private String name;
}
