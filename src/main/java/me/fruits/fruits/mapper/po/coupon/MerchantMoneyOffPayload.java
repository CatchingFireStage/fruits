package me.fruits.fruits.mapper.po.coupon;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.fruits.fruits.utils.MoneyUtils;

/**
 * 商家满减优惠
 * 1. 满多少，减多少
 * 2. 存在多条满减优惠，只能使用其中一条，自动选在优惠最大的那条
 */
@Data
public class MerchantMoneyOffPayload implements Payload {

    @ApiModelProperty(value = "单位分，满足多少钱才生效")
    private int waterLine;

    @ApiModelProperty(value = "单位分，优惠多少钱")
    private int discounts;

    @Override
    public String toString() {
        return "(商家) 满" + MoneyUtils.fenChangeYuan(waterLine) + "减" + MoneyUtils.fenChangeYuan(discounts) + "元";
    }
}
