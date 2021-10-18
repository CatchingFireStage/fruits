package me.fruits.fruits.service.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.service.order.websocket.message.Event;
import me.fruits.fruits.service.order.websocket.message.EventType;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderAdminModuleService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrdersMapper ordersMapper;


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

    /**
     * websocket 获取制作完成订单列表事件
     */
    public String buildWebsocketFulfillOrderListEvent() throws JsonProcessingException {

        return orderService.buildWebsocketFulfillOrderListEvent();
    }

    /**
     * 获取订单列表
     *
     * @param state  状态值
     * @param pageVo 分页
     */
    public IPage<Orders> getOrders(Integer state, PageVo pageVo) {

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        if (state != null) {
            //状态过滤
            queryWrapper.eq("state", state);
        }

        //倒序排序
        queryWrapper.orderByDesc("id");

        return ordersMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }

}
