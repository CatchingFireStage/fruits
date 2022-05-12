package me.fruits.fruits.service.coupon;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.CouponMapper;
import me.fruits.fruits.mapper.enums.coupon.CategoryEnum;
import me.fruits.fruits.mapper.po.Coupon;
import me.fruits.fruits.mapper.po.coupon.MerchantMoneyOffPayload;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.PageVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @return 商家所有满减优惠券, 用户菜单显示给用户看商家活动都有什么
     */
    public List<MerchantMoneyOffPayload> getAllMerchantMoneyOffPayload() {
        List<Coupon> list = lambdaQuery().eq(Coupon::getCategory, CategoryEnum.MERCHANT_MONEY_OFF).list();
        if (list == null) {
            return new ArrayList<>();
        }

        //随便排个序，可有可无
        list.sort((coupon1, coupon2) -> {

            MerchantMoneyOffPayload payload1 = (MerchantMoneyOffPayload) coupon1.getPayload();
            MerchantMoneyOffPayload payload2 = (MerchantMoneyOffPayload) coupon2.getPayload();

            return payload2.getWaterLine() - payload1.getWaterLine();
        });

        return list.stream().map(coupon -> (MerchantMoneyOffPayload) coupon.getPayload()).collect(Collectors.toList());
    }

    /**
     * 创建一个商家满减的优惠券
     *
     * @param waterLine 单位分
     * @param discounts 单位分
     */
    public void createByMerchantMoneyOff(int waterLine, int discounts) {

        Long count = lambdaQuery().eq(Coupon::getCategory, CategoryEnum.MERCHANT_MONEY_OFF)
                .count();

        if (count != null && count >= 10) {
            throw new FruitsRuntimeException("商家满减最多10条");
        }


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

    /**
     * @param money 实际支付金额，单位分
     * @return 获取商家满减最大优惠的卷
     */
    public MerchantMoneyOffPayload getMaxMerchantMoneyOffPayload(int money) {
        List<Coupon> list = lambdaQuery().eq(Coupon::getCategory, CategoryEnum.MERCHANT_MONEY_OFF).list();
        if (list == null) {
            return null;
        }

        //按照优惠价格从高到低排序
        list.sort((coupon1, coupon2) -> {

            MerchantMoneyOffPayload payload1 = (MerchantMoneyOffPayload) coupon1.getPayload();
            MerchantMoneyOffPayload payload2 = (MerchantMoneyOffPayload) coupon2.getPayload();

            return payload2.getWaterLine() - payload1.getWaterLine();

        });

        //返回商家满减最大优惠的卷
        for (Coupon coupon : list) {

            MerchantMoneyOffPayload payload = (MerchantMoneyOffPayload) coupon.getPayload();

            if (payload.getWaterLine() <= money) {
                return payload;
            }
        }

        return null;
    }
}
