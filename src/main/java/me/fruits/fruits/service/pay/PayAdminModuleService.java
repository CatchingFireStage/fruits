package me.fruits.fruits.service.pay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.PayMapper;
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
    private PayMapper payMapper;


    /**
     * 列表
     */
    public IPage<Pay> getPays(String keyword, MerchantTransactionTypeEnum merchantTransactionTypeEnum,
                              PayStateEnum payStateEnum, PageVo pageVo) {


        QueryWrapper<Pay> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("merchant_transaction_type", merchantTransactionTypeEnum.getValue());

        //搜索
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.eq("out_trade_no", Long.valueOf(keyword));
        }

        if (payStateEnum != null) {
            queryWrapper.eq("state", payStateEnum.getValue());
        }

        queryWrapper.orderByDesc("id");


        return payMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }

}
