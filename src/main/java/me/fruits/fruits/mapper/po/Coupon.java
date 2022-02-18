package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.fruits.fruits.mapper.enums.coupon.CategoryEnum;
import me.fruits.fruits.mapper.po.coupon.Payload;

import java.io.Serializable;

@Data
@TableName(value = "coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = -8446363377604338617L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "优惠券类型")
    private CategoryEnum category;

    @ApiModelProperty(value = "优惠券具体是如何优惠的")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Payload payload;
}
