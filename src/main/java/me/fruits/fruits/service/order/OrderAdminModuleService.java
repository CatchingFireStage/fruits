package me.fruits.fruits.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderAdminModuleService {

    @Autowired
    private OrderService orderService;


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

        LambdaQueryChainWrapper<Orders> queryWrapper = orderService.lambdaQuery();

        if (state != null) {
            //状态过滤
            queryWrapper.eq(Orders::getState, state);
        }

        //倒序排序
        queryWrapper.orderByDesc(Orders::getId);

        return queryWrapper.page(new Page<>(pageVo.getP(), pageVo.getPageSize()));
    }

}
