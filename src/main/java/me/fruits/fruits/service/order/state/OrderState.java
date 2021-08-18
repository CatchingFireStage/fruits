package me.fruits.fruits.service.order.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Order;
import me.fruits.fruits.service.order.InputOrderDescriptionDTO;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.MoneyUtils;

import java.time.LocalDateTime;

/**
 * 下单状态
 */
@Slf4j
public class OrderState implements State {

    @Override
    public void doAction(Context context) {

        //当前状态是下单状态
        context.setState(this);

        //获取订单详细
        InputOrderDescriptionDTO inputOrderDescriptionDTO = context.getInputOrderDescriptionDTO();


        //创建订单
        Order order = new Order();
        order.setPayMoney(MoneyUtils.yuanChangeFen(inputOrderDescriptionDTO.getPayAmount()));
        order.setCreateTime(LocalDateTime.now());
        //下单状态
        order.setStatus(0);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //订单详情
            String description = objectMapper.writeValueAsString(inputOrderDescriptionDTO);
            order.setDescription(description);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new FruitsRuntimeException("下单失败，序列化失败");
        }

        //创建下单
        context.getOrderService().add(order);
    }
}
