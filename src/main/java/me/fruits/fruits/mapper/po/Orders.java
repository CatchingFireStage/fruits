package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "orders")
public class Orders implements Serializable {
    private static final long serialVersionUID = 4947804699476359257L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "订单实际支付金额，单位分")
    private Integer payMoney;

    @ApiModelProperty(value = "订单描述，具有一定格式，能够序列化和反序列化")
    private String description;

    @ApiModelProperty(value = "订单状态")
    private Integer State;

    @ApiModelProperty(value = "下单的用户")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
