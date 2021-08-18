package me.fruits.fruits.service.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("存到数据库中的订单格式")
public class InputOrderDescriptionDTO {



    @ApiModel("存到数据库中的订单格式元数据")
    @Data
    class OrderDescriptionDTO {

        @ApiModelProperty(name = "spu",value = "spu商品信息")
        private Spu spu;
    }

    @Data
    static class Spu {

        @ApiModelProperty(name = "name",value = "商品名字")
        private String name;

        @ApiModelProperty(name = "money",value = "商品价格，单位分")
        private Integer money;
    }
}
