package me.fruits.fruits.service.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("下单数据")
@Data
public class InputOrderDescriptionVO {

    @ApiModelProperty(name = "order", value = "下单数据")
    private OrderDescriptionVo[] order;




    /**
     * 用户输入的订单元数据
     */
    @ApiModel("用户输入订单的元数据")
    @Data
    static class OrderDescriptionVo{

        @ApiModelProperty(name = "spuId", value = "spuId")
        private Long spuId;

        @ApiModelProperty(name = "values", value = "规格值")
        private Long[] values;
    }

}
