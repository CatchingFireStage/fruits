package me.fruits.fruits.mapper.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "user_we_chat")
public class UserWeChat implements Serializable {

    private static final long serialVersionUID = -3952175034984122937L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "微信体系的载体。0：小程序, 1: 公众号")
    private Integer carrier;

    @ApiModelProperty(value = "微信的openId")
    private String openId;

    @ApiModelProperty(value = "微信小程序的session_key，用来签名、解密开放数据")
    private String sessionKey;
}
