package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "user")
public class User implements Serializable {
    
    private static final long serialVersionUID = -5880755419958881899L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;
}
