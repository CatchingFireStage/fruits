package me.fruits.fruits.service.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("存到数据库中的订单格式")
public class InputOrderDescriptionDTO {


    @ApiModelProperty(value = "订单详情")
    private List<OrderDescriptionDTO> orderDescription;

    @ApiModelProperty(value = "支付总金额,单位元")
    private String payAmount;

    @ApiModelProperty(value = "用户id")
    private Long userId;


    @ApiModel("存到数据库中的订单格式元数据")
    @Data
    static class OrderDescriptionDTO {

        @ApiModelProperty(value = "spu商品信息")
        private Spu spu;

        @ApiModelProperty(value = "规格值")
        private List<SpuSpecificationValue> spuSpecificationValue;
    }

    @Data
    static class Spu {

        @ApiModelProperty(value = "商品名字")
        private String name;

        @ApiModelProperty(value = "商品价格，单位分")
        private Integer money;
    }

    @Data
    static class SpuSpecificationValue {

        @ApiModelProperty(value = "规格名")
        private String name;

        @ApiModelProperty(value = "规格值")
        private String value;

        @ApiModelProperty(value = "规格值的价格，单位分")
        private Integer money;
    }
}
