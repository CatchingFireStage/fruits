package me.fruits.fruits.service.order;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.po.*;
import me.fruits.fruits.service.spu.SpecificationService;
import me.fruits.fruits.service.spu.SpecificationValueService;
import me.fruits.fruits.service.spu.SpuService;
import me.fruits.fruits.service.user.UserService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Slf4j
public abstract class OrderService {


    @Autowired
    private SpuService spuService;

    @Autowired
    private SpecificationValueService specificationValueService;


    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrdersMapper orderMapper;


    /**
     * 构建生成订单详情
     */
    public InputOrderDescriptionDTO buildInputOrderDescriptionDTO(InputOrderDescriptionVO inputOrderDescriptionVO) {

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

            //输出的spu设置
            InputOrderDescriptionDTO.Spu spuDTO = new InputOrderDescriptionDTO.Spu();
            spuDTO.setMoney(spu.getMoney());
            spuDTO.setName(spu.getName());

            orderDescriptionDTO.setSpu(spuDTO);


            //获取商品的规格
            for (int valueIdsCurIndex = 0; valueIdsCurIndex < inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().size(); valueIdsCurIndex++) {

                SpecificationValue specificationValue = specificationValueService.getSpecificationValue(inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().get(valueIdsCurIndex));
                if (specificationValue == null) {
                    String warn = String.format("商品id:%d;规格值id:%d 不存在", inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId(), inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().get(valueIdsCurIndex));
                    log.warn(warn);
                    throw new FruitsRuntimeException(warn);
                }

                Specification specification = specificationService.getSpecification(specificationValue.getSpecificationId());
                if (specification == null) {
                    String warn = String.format("商品id:%d;规格值id:%d的规格 不存在", inputOrderDescriptionVO.getOrder().get(curIndex).getSpuId(), inputOrderDescriptionVO.getOrder().get(curIndex).getSpecificationValueIds().get(valueIdsCurIndex));
                    log.warn(warn);
                    throw new FruitsRuntimeException(warn);
                }


                //规格唯一验证
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


    public Orders getOrder(long id){
        return this.orderMapper.selectById(id);
    }

    /**
     * 创建订单
     */
    @Transactional
    public void add(Orders orders) {


        //todo: 创建三方的支付订单


        //创建订单
        this.orderMapper.insert(orders);

    }

    /**
     * 更新订单状态为关闭
     * 注意：！！！！！不阅读注释一定扑街
     * 什么时候才会调用这个updateStatusToClose方法请看 {@link me.fruits.fruits.service.order.state.CloseState}
     */
    public void updateStatusToClose(long id) {

        //todo: 关闭三方的支付订单


        //更新订单状态为关闭,下单状态 才能切换到 已关闭状态
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
        .eq("status",0)
        .set("status", 2);

        this.orderMapper.update(null, updateWrapper);
    }
}
