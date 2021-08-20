package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "pay")
public class Pay implements Serializable {
    private static final long serialVersionUID = -4449247737685703956L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "商户系统的订单号")
    private Long ordersId;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "订单失效时间")
    private LocalDateTime timeExpire;

    @ApiModelProperty(value = "金额，单位分")
    private Integer amount;

    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单状态；0下单，1已支付，2支付失败")
    private Integer state;

    @ApiModelProperty(value = "微信支付系统生成的订单号")
    private String transactionId;
}