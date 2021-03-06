package me.fruits.fruits.service.order;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.enums.orders.OrderStateEnum;
import me.fruits.fruits.mapper.enums.pay.MerchantTransactionTypeEnum;
import me.fruits.fruits.mapper.po.*;
import me.fruits.fruits.mapper.po.coupon.MerchantMoneyOffPayload;
import me.fruits.fruits.mapper.po.order.InputOrderDescriptionDTO;
import me.fruits.fruits.service.coupon.CouponService;
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
public class OrderService extends ServiceImpl<OrdersMapper, Orders> {


    @Autowired
    private SpuService spuService;

    @Autowired
    private SpecificationValueService specificationValueService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private PayService payService;

    @Autowired
    private SpecificationSpuService specificationSpuService;

    @Autowired
    private CouponService couponService;

    /**
     * 构建订单步骤，验证用户
     *
     * @return 返回有效用户的id
     */
    private long encodeInputOrderDescriptionDTOStepVerifyUser(long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            log.warn("用户不存在");
            throw new FruitsRuntimeException("用户不存在");
        }
        return userId;
    }

    /**
     * 优惠券步骤
     *
     * @param inputOrderDescriptionDTO 订单原价总金额
     */
    private void encodeInputOrderDescriptionDTOStepCoupon(InputOrderDescriptionDTO inputOrderDescriptionDTO) {

        //商家满减优惠券
        MerchantMoneyOffPayload maxMerchantMoneyOffPayload = couponService.getMaxMerchantMoneyOffPayload(inputOrderDescriptionDTO.getPayAmount());
        if (maxMerchantMoneyOffPayload != null) {
            //参与满减活动后的金额 = 减去满减金额
            int payAmount = inputOrderDescriptionDTO.getPayAmount() - maxMerchantMoneyOffPayload.getDiscounts();
            inputOrderDescriptionDTO.setPayAmount(Math.max(payAmount, 0));
            inputOrderDescriptionDTO.getCouponInfo().add(maxMerchantMoneyOffPayload.toString());
        }

    }

    /**
     * 构建生成订单详情
     *
     * @param userId 下单的用户
     */
    public InputOrderDescriptionDTO encodeInputOrderDescriptionDTO(long userId, InputOrderDescriptionVO inputOrderDescriptionVO) {

        InputOrderDescriptionDTO inputOrderDescriptionDTO = new InputOrderDescriptionDTO();

        //桌号
        inputOrderDescriptionDTO.setDesk(inputOrderDescriptionVO.getDesk());

        inputOrderDescriptionDTO.setOrderDescription(new ArrayList<>());

        //用户验证，步骤1
        inputOrderDescriptionDTO.setUserId(encodeInputOrderDescriptionDTOStepVerifyUser(userId));


        //构建每一条商品，步骤2

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

            if (spu.getIsInventory().equals(0)) {
                String warn = String.format("商品id:%d,名字:%s,卖完了,无货", spu.getId(), spu.getName());
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


        //计算总金额（实际支付金额，未参加优惠券前），步骤3
        int payAmount = 0;

        for (int i = 0; i < inputOrderDescriptionDTO.getOrderDescription().size(); i++) {

            //spu的价格
            payAmount += inputOrderDescriptionDTO.getOrderDescription().get(i).getSpu().getMoney();

            //spu规格的价格
            for (int j = 0; j < inputOrderDescriptionDTO.getOrderDescription().get(i).getSpuSpecificationValue().size(); j++) {
                payAmount += inputOrderDescriptionDTO.getOrderDescription().get(i).getSpuSpecificationValue().get(j).getMoney();
            }
        }


        inputOrderDescriptionDTO.setPayAmount(payAmount);


        //优惠券步骤
        encodeInputOrderDescriptionDTOStepCoupon(inputOrderDescriptionDTO);

        return inputOrderDescriptionDTO;
    }


    public Orders getOrder(long id) {
        return getById(id);
    }


    /**
     * 创建订单
     */
    @Transactional
    public WxPayUnifiedOrderV3Result.JsapiResult addOrderByJSAPI(long userId, InputOrderDescriptionVO inputOrderDescriptionVO) {

        //生成订单详情
        InputOrderDescriptionDTO inputOrderDescriptionDTO = encodeInputOrderDescriptionDTO(userId, inputOrderDescriptionVO);


        //创建订单
        Orders orders = new Orders();
        orders.setPayMoney(inputOrderDescriptionDTO.getPayAmount());
        orders.setCreateTime(LocalDateTime.now());
        orders.setUserId(inputOrderDescriptionDTO.getUserId());
        //下单状态
        orders.setState(OrderStateEnum.ORDER.getValue());

        orders.setDescription(inputOrderDescriptionDTO);


        //订单入库
        save(orders);

        try {

            //todo: 创建三方的支付订单、三方订单入库
            return payService.orderJSPAPI(userId, orders.getId(), MerchantTransactionTypeEnum.ORDER, orders);
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new FruitsRuntimeException("下单失败,三方失败");
        }
    }


    public void updateStatusToPay(Long id) {

        if (!this.updateStatusToPay(id.longValue())) {
            //更新失败，结束
            String format = String.format("更新订单id:%s到已支付失败", id);
            log.error(format);
            throw new FruitsRuntimeException(format);
        }


        //更新成功触发一些事件
        try {
            Orders order = getOrder(id);
            //websocket订单通知
            eventHandler.producerNewPayOrderNotify(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新订单状态为已支付； 下单 才能切换到 已支付
     */
    private boolean updateStatusToPay(long id) {
        //更新订单状态为完成制作, 已支付 才能切换到 完成状态
        return lambdaUpdate().eq(Orders::getId, id)
                .eq(Orders::getState, OrderStateEnum.ORDER.getValue())
                .set(Orders::getState, OrderStateEnum.PAY.getValue())
                .update();
    }


    public void updateStatusToFulfill(Long id) {

        Orders orders = getOrder(id);

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
        return lambdaUpdate().eq(Orders::getId, id)
                .eq(Orders::getState, OrderStateEnum.PAY.getValue())
                .set(Orders::getState, OrderStateEnum.COMPLETED.getValue()).update();
    }


    /**
     * 更新订单状态为送达, 制作完成 才能切换到 送达
     */
    public void updateStatusToDelivery(long id) {

        //更新订单状态为送达, 制作完成 才能切换到 送达
        lambdaUpdate().eq(Orders::getId, id)
                .eq(Orders::getState, OrderStateEnum.COMPLETED.getValue())
                .set(Orders::getState, OrderStateEnum.DELIVERY.getValue())
                .update();

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
        lambdaUpdate().eq(Orders::getId, id)
                .eq(Orders::getState, OrderStateEnum.ORDER.getValue())
                .set(Orders::getState, OrderStateEnum.CLOSE.getValue())
                .update();
    }


    /**
     * 获取已支付的订单或者是完成制作的订单
     */
    private List<Map<String, Object>> getOrdersByPayStateOrFulfillState(OrderStateEnum orderStateEnum) {

        if (orderStateEnum != OrderStateEnum.PAY && orderStateEnum != OrderStateEnum.COMPLETED) {
            throw new FruitsRuntimeException("该方法只能获取已支付或者完整制作的订单");
        }


        List<Orders> orders = lambdaQuery().eq(Orders::getState, orderStateEnum.getValue()).list();


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
        item.put("description", order.getDescription());
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


    /**
     * 基与后端的inputOrderDescriptionDTO结构，包装、处理、转化成前端的展示数据
     *
     * @param inputOrderDescriptionDTO 后端的结构
     * @return
     */
    public static Map<String, Object> inputOrderDescriptionDTOWrap(InputOrderDescriptionDTO inputOrderDescriptionDTO) {

        Map<String, Object> response = new HashMap<>();

        //订单详情
        List<Object> orderDescription = new ArrayList<>();

        inputOrderDescriptionDTO.getOrderDescription().forEach(orderDescriptionDTO -> {

            Map<String, Object> orderDescriptionItemRow = new HashMap<>();

            //spu
            Map<String, Object> spu = new HashMap<>();

            spu.put("money", MoneyUtils.fenChangeYuan(orderDescriptionDTO.getSpu().getMoney()));
            spu.put("name", orderDescriptionDTO.getSpu().getName());

            orderDescriptionItemRow.put("spu", spu);

            //spu的规格值
            List<Object> spuSpecificationValue = new ArrayList<>();

            orderDescriptionDTO.getSpuSpecificationValue().forEach(spuSpecificationValueItem -> {

                Map<String, Object> spuSpecificationValueItemRow = new HashMap<>();

                spuSpecificationValueItemRow.put("money", MoneyUtils.fenChangeYuan(spuSpecificationValueItem.getMoney()));
                spuSpecificationValueItemRow.put("name", spuSpecificationValueItem.getName());
                spuSpecificationValueItemRow.put("value", spuSpecificationValueItem.getValue());


                spuSpecificationValue.add(spuSpecificationValueItemRow);
            });


            orderDescriptionItemRow.put("spuSpecificationValue", spuSpecificationValue);

            orderDescription.add(orderDescriptionItemRow);

        });


        response.put("orderDescription", orderDescription);

        //订单总金额
        response.put("payAmount", MoneyUtils.fenChangeYuan(inputOrderDescriptionDTO.getPayAmount()));
        //优惠信息
        response.put("couponInfo", inputOrderDescriptionDTO.getCouponInfo());


        return response;
    }
}
