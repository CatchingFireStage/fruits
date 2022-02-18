package me.fruits.fruits.mapper.po.coupon;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MerchantMoneyOffPayload implements Payload {

    @ApiModelProperty(value = "单位分，满足多少钱才生效")
    private int waterLine;

    @ApiModelProperty(value = "单位分，优惠多少钱")
    private int discounts;
}
