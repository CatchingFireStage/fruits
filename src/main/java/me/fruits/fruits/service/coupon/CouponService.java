package me.fruits.fruits.service.coupon;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.CouponMapper;
import me.fruits.fruits.mapper.enums.coupon.CategoryEnum;
import me.fruits.fruits.mapper.po.Coupon;
import me.fruits.fruits.mapper.po.coupon.MerchantMoneyOffPayload;
import me.fruits.fruits.utils.PageVo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponService extends ServiceImpl<CouponMapper, Coupon> {


    /**
     * 后台管理用的列表接口
     *
     * @return
     */
    public IPage<Coupon> getCouponsByAdminManage(PageVo pageVo) {
        return lambdaQuery().page(new Page<>(pageVo.getP(), pageVo.getPageSize()));
    }

    /**
     * 创建一个商家满减的优惠券
     *
     * @param waterLine 单位分
     * @param discounts 单位分
     */
    public void createByMerchantMoneyOff(int waterLine, int discounts) {
        Coupon coupon = new Coupon();
        coupon.setCategory(CategoryEnum.MERCHANT_MONEY_OFF);
        MerchantMoneyOffPayload merchantMoneyOffPayload = new MerchantMoneyOffPayload();
        merchantMoneyOffPayload.setWaterLine(waterLine);
        merchantMoneyOffPayload.setDiscounts(discounts);
        coupon.setPayload(merchantMoneyOffPayload);
        save(coupon);
    }

    public void delete(long id) {
        removeById(id);
    }
}
