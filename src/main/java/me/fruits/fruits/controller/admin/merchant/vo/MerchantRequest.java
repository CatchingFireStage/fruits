package me.fruits.fruits.controller.admin.merchant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel("商家信息")
public class MerchantRequest {
    //营业开始时间
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "startTime", value = "营业开始时间", required = true)
    private LocalDateTime startTime;

    //营业结束时间
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "endTime", value = "营业结束时间", required = true)
    private LocalDateTime endTime;

    //是否24小时
    @NotNull
    @ApiModelProperty(name = "is24Hours", value = "是否24小时", required = true)
    private Boolean is24Hours;

    //是否关门休息
    @NotNull
    @ApiModelProperty(name = "isClose", value = "是否休息", required = true)
    private Boolean isClose;
}
