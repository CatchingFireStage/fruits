package me.fruits.fruits.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.websocket.message.Event;
import me.fruits.fruits.service.order.websocket.message.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderAdminModuleService extends OrderService {

    @Autowired
    private OrderService orderService;


    /**
     * 订单预览
     */
    public InputOrderDescriptionDTO encodeInputOrderDescriptionDTO(InputOrderDescriptionVO inputOrderDescriptionVO) {

        return orderService.encodeInputOrderDescriptionDTO(inputOrderDescriptionVO);
    }


    public String addOrderByNative(InputOrderDescriptionVO inputOrderDescriptionVO) {
        return orderService.addOrderByNative(inputOrderDescriptionVO);
    }


    public void updateStatusToFulfill(Long id) {
        orderService.updateStatusToFulfill(id);
    }

    public void updateStatusToDelivery(long id) {
        orderService.updateStatusToDelivery(id);
    }


    /**
     * websocket 获取支付订单列表事件
     */
    public String buildWebsocketPayOrderListEvent() throws JsonProcessingException {

        return orderService.buildWebsocketPayOrderListEvent();
    }

}
