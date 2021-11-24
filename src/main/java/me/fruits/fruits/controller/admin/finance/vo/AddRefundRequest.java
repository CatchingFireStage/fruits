package me.fruits.fruits.controller.admin.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 申请退款
 */
@Data
public class AddRefundRequest {

    @ApiModelProperty(value = "支付id", name = "payId", required = true)
    @NotNull(message = "支付id不能为空")
    private Long payId;

    @ApiModelProperty(value = "退款原因", name = "reason", required = true, example = "")
    @NotNull(message = "退款原因")
    private String reason;

    @ApiModelProperty(value = "退款金额,单位元", name = "amount", required = true, example = "1.00")
    @NotNull
    @Pattern(regexp = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$", message = "金额格式不对")
    private String amount;
}
