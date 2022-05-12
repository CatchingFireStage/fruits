package me.fruits.fruits.service.pay;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.enums.pay.MerchantTransactionTypeEnum;
import me.fruits.fruits.mapper.enums.pay.PayStateEnum;
import me.fruits.fruits.mapper.po.Pay;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayAdminModuleService {

    @Autowired
    private PayService payService;


    /**
     * 列表
     */
    public IPage<Pay> getPays(String keyword, MerchantTransactionTypeEnum merchantTransactionTypeEnum,
                              PayStateEnum payStateEnum, PageVo pageVo) {


        LambdaQueryChainWrapper<Pay> queryWrapper = payService.lambdaQuery();

        queryWrapper.eq(Pay::getMerchantTransactionType, merchantTransactionTypeEnum.getValue());

        //搜索
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.eq(Pay::getOutTradeNo, Long.valueOf(keyword));
        }

        if (payStateEnum != null) {
            queryWrapper.eq(Pay::getState, payStateEnum.getValue());
        }

        queryWrapper.orderByDesc(Pay::getId);

        return payService.page(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }

}
