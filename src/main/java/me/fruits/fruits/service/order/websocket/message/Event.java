package me.fruits.fruits.service.order.websocket.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Event {
    @ApiModelProperty(value = "事件类型")
    private EventType eventType;

    @ApiModelProperty(value = "事件")
    private String event;
}
