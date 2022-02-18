package me.fruits.fruits.controller.admin.merchant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@ApiModel("优惠券")
public class CouponRequest {

    @ApiModelProperty(value = "单位元，商家满减")
    @Range(min = 0,max = 3000,message = "0-3000元范围内")
    private String waterLine;

    @ApiModelProperty(value = "单位元，商检满减")
    @Range(min = 0,max = 3000,message = "0-3000元范围内")
    private String discounts;
}
