package me.fruits.fruits.service.order;


import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.mapper.po.SpecificationValue;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.spu.SpecificationService;
import me.fruits.fruits.service.spu.SpecificationValueService;
import me.fruits.fruits.service.spu.SpuService;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


@Slf4j
public abstract class OrderService {


    @Autowired
    private SpuService spuService;

    @Autowired
    private SpecificationValueService specificationValueService;


    @Autowired
    private SpecificationService specificationService;


    /**
     * 构建生成订单信息
     */
    public InputOrderDescriptionDTO buildOrder(InputOrderDescriptionVO inputOrderDescriptionVO) {

        InputOrderDescriptionDTO inputOrderDescriptionDTO = new InputOrderDescriptionDTO();

        inputOrderDescriptionDTO.setOrderDescription(new ArrayList<>());

        //构建每一条数据
        inputOrderDescriptionVO.getOrder().forEach(orderDescriptionVo -> {


            InputOrderDescriptionDTO.OrderDescriptionDTO orderDescriptionDTO = new InputOrderDescriptionDTO.OrderDescriptionDTO();

            orderDescriptionDTO.setSpuSpecificationValue(new ArrayList<>());


            //获取商品
            Spu spu = spuService.getSpu(orderDescriptionVo.getSpuId());
            if (spu == null) {
                String warn = String.format("商品id:%d 不存在", orderDescriptionVo.getSpuId());
                log.warn(warn);
                throw new FruitsRuntimeException(warn);
            }

            //输出的spu设置
            InputOrderDescriptionDTO.Spu spuDTO = new InputOrderDescriptionDTO.Spu();
            spuDTO.setMoney(spu.getMoney());
            spuDTO.setName(spu.getName());

            orderDescriptionDTO.setSpu(spuDTO);


            //获取商品的规格
            orderDescriptionVo.getSpecificationValueIds().forEach(specificationValueId -> {

                SpecificationValue specificationValue = specificationValueService.getSpecificationValue(specificationValueId);
                if (specificationValue == null) {
                    String warn = String.format("商品id:%d;规格值id:%d 不存在", orderDescriptionVo.getSpuId(), specificationValueId);
                    log.warn(warn);
                    throw new FruitsRuntimeException(warn);
                }

                Specification specification = specificationService.getSpecification(specificationValue.getSpecificationId());
                if (specification == null) {
                    String warn = String.format("商品id:%d;规格值id:%d的规格 不存在", orderDescriptionVo.getSpuId(), specificationValueId);
                    log.warn(warn);
                    throw new FruitsRuntimeException(warn);
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
            });


            inputOrderDescriptionDTO.getOrderDescription().add(orderDescriptionDTO);

        });



        //总金额
        int payAmount = 0;

        for(int i=0; i< inputOrderDescriptionDTO.getOrderDescription().size(); i++){

            //spu的价格
            payAmount += inputOrderDescriptionDTO.getOrderDescription().get(i).getSpu().getMoney();

            //spu规格的价格
            for(int j = 0; j <  inputOrderDescriptionDTO.getOrderDescription().get(i).getSpuSpecificationValue().size(); j++){
                payAmount +=  inputOrderDescriptionDTO.getOrderDescription().get(i).getSpuSpecificationValue().get(j).getMoney();
            }
        }


        inputOrderDescriptionDTO.setPayAmount(MoneyUtils.fenChangeYuan(payAmount));

        return inputOrderDescriptionDTO;
    }
}
