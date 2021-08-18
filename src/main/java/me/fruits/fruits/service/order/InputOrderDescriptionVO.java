package me.fruits.fruits.service.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@ApiModel("下单数据")
@Data
public class InputOrderDescriptionVO {

    @ApiModelProperty(value = "下单数据", required = true)
    private List<OrderDescriptionVo> order;


    /**
     * 用户输入的订单元数据
     */
    @ApiModel("用户输入订单的元数据")
    @Data
    static class OrderDescriptionVo {

        @ApiModelProperty(value = "spuId唯一标识")
        private Long spuId;

        @ApiModelProperty(value = "规格值唯一标识")
        private List<Long> specificationValueIds;
    }

}
