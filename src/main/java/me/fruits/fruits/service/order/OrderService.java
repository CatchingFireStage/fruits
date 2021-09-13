package me.fruits.fruits.service.order;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.enums.OrderStateEnum;
import me.fruits.fruits.mapper.po.*;
import me.fruits.fruits.service.order.websocket.EventHandler;
import me.fruits.fruits.service.order.websocket.message.Event;
import me.fruits.fruits.service.order.websocket.message.EventType;
import me.fruits.fruits.service.pay.PayService;
import me.fruits.fruits.service.spu.SpecificationService;
import me.fruits.fruits.service.spu.SpecificationSpuService;
import me.fruits.fruits.service.spu.SpecificationValueService;
import me.fruits.fruits.service.spu.SpuService;
import me.fruits.fruits.service.user.UserService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
public class OrderService {


    @Autowired
    private SpuService spuService;

    @Autowired
    private SpecificationValueService specificationValueService;


    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private PayService payService;

    @Autowired
    private SpecificationSpuService specificationSpuService;

    /**
     * 构建生成订单详情
     */
    public InputOrderDescriptionDTO encodeInputOrderDescriptionDTO(InputOrderDescriptionVO inputOrderDescriptionVO) {

        InputOrderDescriptionDTO inputOrderDescriptionDTO = new InputOrderDescriptionDTO();

        inputOrderDescriptionDTO.setOrderDescription(new ArrayList<>());

        //用户验证
        User user = userService.getUser(inputOrderDescriptionVO.getUserId());
        if (user == null) {
            log.warn("用户不存在");
            throw new FruitsRuntimeException("用户不存在");
        }

        inputOrderDescriptionDTO.setUserId(user.getId());

        //规格唯一验证
        Set<String> specificationUniq = new HashSet<>();


        //构建每一条数据

        for (int curIndex = 0; curIndex < inputOrderDescriptionVO.getOrder().size(); curIndex++) {


            InputOrderDescriptionDTO.OrderDescriptionDTO orderDescriptionDTO = new InputOrderDescriptionDTO.OrderDescriptionDTO();

            orderDescriptionDTO.setSpuSpecificationValue(new ArrayList<>());


            //获取商品
            Spu spu = spuService.getSpu(inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId());
            if (spu == null) {
                String warn = String.format("商品id:%d 不存在", inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId());
                log.warn(warn);
                throw new FruitsRuntimeException(warn);
            }

            if(spu.getIsInventory().equals(0)){
                String warn = String.format("商品id:%d,名字:%s,卖完了,无货",spu.getId(),spu.getName());
                log.warn(warn);
                throw new FruitsRuntimeException(warn);
            }

            //输出的spu设置
            InputOrderDescriptionDTO.Spu spuDTO = new InputOrderDescriptionDTO.Spu();
            spuDTO.setMoney(spu.getMoney());
            spuDTO.setName(spu.getName());

            orderDescriptionDTO.setSpu(spuDTO);


            //获取spu必选的规格
            List<SpecificationSpu> specificationSpuRequired = specificationSpuService.getSpecificationSpuRequiredBySpuId(spu.getId());
            Set<Long> requiredSpecificationIds = new HashSet<>();
            specificationSpuRequired.forEach(specificationSpu -> {
                requiredSpecificationIds.add(specificationSpu.getSpecificationId());
            });

            //单个spu中验证是否都选了必填项
            int orderItemRequiredNumber = 0;


            //获取商品的规格
            for (int valueIdsCurIndex = 0; valueIdsCurIndex < inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().size(); valueIdsCurIndex++) {

                //规格值
                SpecificationValue specificationValue = specificationValueService.getSpecificationValue(inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().get(valueIdsCurIndex));
                if (specificationValue == null) {
                    String warn = String.format("商品id:%d;规格值id:%d 不存在", inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId(), inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().get(valueIdsCurIndex));
                    log.warn(warn);
                    throw new FruitsRuntimeException(warn);
                }

                //规格
                Specification specification = specificationService.getSpecification(specificationValue.getSpecificationId());
                if (specification == null) {
                    String warn = String.format("商品id:%d;规格值id:%d的规格 不存在", inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId(), inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().get(valueIdsCurIndex));
                    log.warn(warn);
                    throw new FruitsRuntimeException(warn);
                }

                //必选规格验证
                if (requiredSpecificationIds.contains(specification.getId())) {
                    orderItemRequiredNumber++;
                }


                //同种规格唯一验证
                String specificationUniqKey = String.format("index:%d,spuId:%d,specificationId:%d", curIndex, inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId(), specification.getId());

                if (!specificationUniq.add(specificationUniqKey)) {
                    //添加失败,同类规格出现重复，抛出异常
                    throw new FruitsRuntimeException(String.format("存在同种类型的规格:%s", specification.getName()));
                }


                //输出的spu规格设置
                InputOrderDescriptionDTO.SpuSpecificationValue spuSpecificationValueDTO = new InputOrderDescriptionDTO.SpuSpecificationValue();

                //规格的价格
                spuSpecificationValueDTO.setMoney(specificationValue.getMoney());
                //规格的具体名字
                spuSpecificationValueDTO.setValue(specificationValue.getValue());
                //规格名
                spuSpecificationValueDTO.setName(specification.getName());


                orderDescriptionDTO.getSpuSpecificationValue().add(spuSpecificationValueDTO);

            }


            if (orderItemRequiredNumber != requiredSpecificationIds.size()) {
                throw new FruitsRuntimeException("缺少必选规格");
            }

            inputOrderDescriptionDTO.getOrderDescription().add(orderDescriptionDTO);
        }


        //总金额
        int payAmount = 0;

        for (int i = 0; i < inputOrderDescriptionDTO.getOrderDescription().size(); i++) {

            //spu的价格
            payAmount += inputOrderDescriptionDTO.getOrderDescription().get(i).getSpu().getMoney();

            //spu规格的价格
            for (int j = 0; j < inputOrderDescriptionDTO.getOrderDescription().get(i).getSpuSpecificationValue().size(); j++) {
                payAmount += inputOrderDescriptionDTO.getOrderDescription().get(i).getSpuSpecificationValue().get(j).getMoney();
            }
        }


        inputOrderDescriptionDTO.setPayAmount(MoneyUtils.fenChangeYuan(payAmount));

        return inputOrderDescriptionDTO;
    }

    public InputOrderDescriptionDTO decodeInputOrderDescriptionDTO(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, InputOrderDescriptionDTO.class);
    }


    public Orders getOrder(long id) {
        return this.ordersMapper.selectById(id);
    }


    /**
     * 创建订单
     */
    @Transactional
    public String addOrderByNative(InputOrderDescriptionVO inputOrderDescriptionVO) {

        //生成订单详情
        InputOrderDescriptionDTO inputOrderDescriptionDTO = encodeInputOrderDescriptionDTO(inputOrderDescriptionVO);


        //创建订单
        Orders orders = new Orders();
        orders.setPayMoney(MoneyUtils.yuanChangeFen(inputOrderDescriptionDTO.getPayAmount()));
        orders.setCreateTime(LocalDateTime.now());
        orders.setUserId(inputOrderDescriptionDTO.getUserId());
        //下单状态
        orders.setState(OrderStateEnum.ORDER.getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //订单详情
            String description = objectMapper.writeValueAsString(inputOrderDescriptionDTO);
            orders.setDescription(description);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new FruitsRuntimeException("下单失败，序列化失败");
        }


        //订单入库
        int merchantTransactionId = this.ordersMapper.insert(orders);

//        try {
//
//            //todo: 创建三方的支付订单、三方订单入库
//            payService.orderNative(merchantTransactionId, PayService.MerchantTransactionTypeEnum.ORDER, orders);
//        } catch (WxPayException e) {
//            e.printStackTrace();
//            throw new FruitsRuntimeException("下单失败,三方失败");
//        }

        return "http://www.baidu.com";
    }


    public void updateStatusToPay(Orders orders) {

        if (!this.updateStatusToPay(orders.getId())) {
            //更新失败，结束
            return;
        }

        //更新成功触发一些事件
        eventHandler.producerNewPayOrderNotify(orders);
    }

    /**
     * 更新订单状态为已支付； 下单 才能切换到 已支付
     */
    private boolean updateStatusToPay(long id) {
        //更新订单状态为完成制作, 已支付 才能切换到 完成状态
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .eq("state", OrderStateEnum.ORDER.getValue())
                .set("state", OrderStateEnum.PAY.getValue());

        return this.ordersMapper.update(null, updateWrapper) > 0;
    }


    public void updateStatusToFulfill(Long id) {

        Orders orders = this.ordersMapper.selectById(id);

        if (!updateStatusToFulfill(orders.getId().longValue())) {
            //更新失败
            return;
        }

        //更新成功触发一些事件
        this.eventHandler.producerNewFulfillOrderNotify(orders);
    }

    /**
     * 更新订单状态为完成制作, 已支付 才能切换到 完成状态
     */
    private boolean updateStatusToFulfill(long id) {

        //更新订单状态为完成制作, 已支付 才能切换到 完成状态
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .eq("state", OrderStateEnum.PAY.getValue())
                .set("state", OrderStateEnum.COMPLETED.getValue());

        return this.ordersMapper.update(null, updateWrapper) > 0;
    }


    /**
     * 更新订单状态为送达, 制作完成 才能切换到 送达
     */
    public void updateStatusToDelivery(long id) {

        //更新订单状态为送达, 制作完成 才能切换到 送达
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .eq("state",OrderStateEnum.COMPLETED.getValue() )
                .set("state", OrderStateEnum.DELIVERY.getValue());

        this.ordersMapper.update(null, updateWrapper);

    }

    /**
     * 更新订单状态为关闭,下单状态 才能切换到 已关闭状态
     * 微信 以下情况需要调用关单接口：
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * <p>
     * 需要定时的主动查询订单状态，如果发现支付失败了，关闭订单。
     */
    public void updateStatusToClose(long id) {

        //todo: 关闭三方的支付订单


        //更新订单状态为关闭,下单状态 才能切换到 已关闭状态
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .eq("state", OrderStateEnum.ORDER.getValue())
                .set("state", OrderStateEnum.CLOSE.getValue());

        this.ordersMapper.update(null, updateWrapper);
    }


    /**
     * 获取已支付的订单或者是完成制作的订单
     */
    private List<Map<String, Object>> getOrdersByPayStateOrFulfillState(OrderStateEnum orderStateEnum) {

        if (orderStateEnum != OrderStateEnum.PAY && orderStateEnum != OrderStateEnum.COMPLETED) {
            throw new FruitsRuntimeException("该方法只能获取已支付或者完整制作的订单");
        }

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", orderStateEnum.getValue());
        List<Orders> orders = this.ordersMapper.selectList(queryWrapper);


        List<Map<String, Object>> response = new ArrayList<>();
        orders.forEach(order -> {
            try {
                response.add(buildWebsocketEventDataItem(order));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error(String.format("InputOrderDescriptionDTO反序列化失败,订单id:%d", order.getId()));
            }
        });

        return response;
    }


    private Map<String, Object> buildWebsocketEventDataItem(Orders order) throws JsonProcessingException {
        Map<String, Object> item = new HashMap<>();
        item.put("id", order.getId());
        item.put("description", decodeInputOrderDescriptionDTO(order.getDescription()));
        return item;
    }


    /**
     * websocket 获取支付订单列表事件
     */
    public String buildWebsocketPayOrderListEvent() throws JsonProcessingException {

        Event event = new Event();
        event.setEventType(EventType.PAY_ORDER_LIST);
        event.setEvent(this.objectMapper.writeValueAsString(getOrdersByPayStateOrFulfillState(OrderStateEnum.PAY)));
        return this.objectMapper.writeValueAsString(event);
    }

    /**
     * websocket 获取支付订单事件
     */
    public String buildWebsocketPayOrderEvent(Orders orders) throws JsonProcessingException {


        Event event = new Event();
        event.setEventType(EventType.PAY_ORDER);
        event.setEvent(this.objectMapper.writeValueAsString(buildWebsocketEventDataItem(orders)));

        return this.objectMapper.writeValueAsString(event);
    }


    /**
     * websocket 获取制作完成订单事件
     */
    public String buildWebsocketFulfillOrderEvent(Orders orders) throws JsonProcessingException {

        Event event = new Event();
        event.setEventType(EventType.FULFILL_ORDER);
        event.setEvent(this.objectMapper.writeValueAsString(buildWebsocketEventDataItem(orders)));

        return this.objectMapper.writeValueAsString(event);
    }

    /**
     * websocket 获取制作完成订单列表事件
     */
    public String buildWebsocketFulfillOrderListEvent() throws JsonProcessingException {
        Event event = new Event();
        event.setEventType(EventType.FULFILL_ORDER_LIST);
        event.setEvent(this.objectMapper.writeValueAsString(getOrdersByPayStateOrFulfillState(OrderStateEnum.COMPLETED)));
        return this.objectMapper.writeValueAsString(event);
    }
}
