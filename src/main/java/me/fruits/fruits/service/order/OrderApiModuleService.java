package me.fruits.fruits.service.order;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.MoneyUtils;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderApiModuleService {


    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 获取我的订单列表
     *
     * @param userId 用户id
     */
    public IPage<Orders> getMyOrders(long userId, PageVo pageVo) {

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        //只能看1已支付，3制作完成, 4已取餐
        queryWrapper.in("state", Arrays.asList(1, 3, 4));

        return ordersMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }

    /**
     * 将状态转化为文本
     *
     * @param state 数据库中的状态
     * @return 客户端看到的状态提示
     */
    public String changeStateToText(int state) {
        switch (state) {
            case 0:
                return "待支付";
            case 1:
            case 3:
                return "制作中";
            case 4:
                return "已完成";
            default:
                throw new FruitsRuntimeException("客户只能看到以上定义的订单状态");
        }
    }


    /**
     * 基与后端的inputOrderDescriptionDTO结构，包装、处理、转化成前端的数据
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


        return response;
    }
}
