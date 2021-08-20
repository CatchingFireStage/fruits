package me.fruits.fruits.mapper.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "refund")
public class Refund implements Serializable {

    private static final long serialVersionUID = 8736059897458013706L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;


    @ApiModelProperty(value = "支付表的id")
    private Long payId;


    @ApiModelProperty(value = "退款原因")
    private String reason;

    @ApiModelProperty(value = "退款金额，单位分")
    private Integer amount;

    @ApiModelProperty("0退款中，1退款成功")
    private Integer state;

    @ApiModelProperty(value = "申请退款时间")
    private LocalDateTime createTime;
}
