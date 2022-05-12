package me.fruits.fruits.service.pay.refund;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.enums.pay.refund.RefundStateEnum;
import me.fruits.fruits.mapper.po.Refund;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundAdminModuleService {

    @Autowired
    private RefundService refundService;

    /**
     * 获取退款列表
     *
     * @param keyword     搜索关键字
     * @param refundState 退款状态
     */
    public IPage<Refund> getRefunds(String keyword, RefundStateEnum refundState, PageVo pageVo) {

        LambdaQueryChainWrapper<Refund> queryWrapper = refundService.lambdaQuery();

        if (keyword != null && !keyword.isEmpty()) {
            //支付id搜索
            queryWrapper.eq(Refund::getPayId, keyword);
        }

        if (refundState != null) {
            queryWrapper.eq(Refund::getState, refundState.getValue());
        }

        queryWrapper.orderByDesc(Refund::getId);

        return queryWrapper.page(new Page<>(pageVo.getP(), pageVo.getPageSize()));
    }
}
