package me.fruits.fruits.service.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@ApiModel("下单数据")
@Data
public class InputOrderDescriptionVO {

    @ApiModelProperty(value = "下单数据")
    private List<OrderDescriptionVo> order;


    /**
     * 用户输入的订单元数据
     */
    @ApiModel("用户输入订单的元数据")
    @Data
    static class OrderDescriptionVo {

        @ApiModelProperty(value = "spuId")
        private Long spuId;

        @ApiModelProperty(value = "规格值")
        private List<Long> specificationValueIds;
    }

}
