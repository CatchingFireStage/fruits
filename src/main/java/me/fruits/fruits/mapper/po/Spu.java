package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "spu")
public class Spu implements Serializable {

    private static final long serialVersionUID = 851089217851082768L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("唯一标识")
    private Long id;

    @ApiModelProperty("商品名字")
    private String name;

    @ApiModelProperty("商品分类id")
    private Long categoryId;

    @ApiModelProperty("是否有货：0没有货：1有货")
    private Integer isInventory;

    @ApiModelProperty("商品图片")
    private String image;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
