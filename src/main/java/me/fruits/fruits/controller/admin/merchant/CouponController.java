package me.fruits.fruits.controller.admin.merchant;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.merchant.vo.CouponRequest;
import me.fruits.fruits.controller.admin.merchant.vo.MerchantRequest;
import me.fruits.fruits.mapper.enums.coupon.CategoryEnum;
import me.fruits.fruits.mapper.po.Coupon;
import me.fruits.fruits.mapper.po.coupon.MerchantMoneyOffPayload;
import me.fruits.fruits.service.coupon.CouponService;
import me.fruits.fruits.utils.MoneyUtils;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/merchant")
@RestController(value = "CouponController")
@Api(tags = "商家-优惠券")
@AdminLogin
@Validated
@Slf4j
public class CouponController {

    @Autowired
    private CouponService couponService;


    @GetMapping("/coupon")
    @ApiOperation("优惠券列表")
    public Result<Object> coupon(PageVo pageVo) {

        IPage<Coupon> page = couponService.getCouponsByAdminManage(pageVo);

        if (page.getRecords().size() == 0) {
            return Result.success(page.getTotal(), page.getPages(), new ArrayList<>());
        }

        List<Object> response = new ArrayList<>();

        page.getRecords().forEach(coupon -> {

            Map<String, Object> item = new HashMap<>();

            item.put("id", coupon.getId());
            item.put("category", coupon.getCategory());
            item.put("payload", null);
            switch (coupon.getCategory()) {
                case MERCHANT_MONEY_OFF:
                    //商家满减优惠券
                    MerchantMoneyOffPayload merchantMoneyOff = (MerchantMoneyOffPayload) coupon.getPayload();
                    Map<String, String> merchantMoneyOffObject = new HashMap<>();
                    merchantMoneyOffObject.put("waterLine", MoneyUtils.fenChangeYuan(merchantMoneyOff.getWaterLine()));
                    merchantMoneyOffObject.put("discounts", MoneyUtils.fenChangeYuan(merchantMoneyOff.getDiscounts()));
                    item.put("payload", merchantMoneyOffObject);
                    break;
            }

            response.add(item);
        });
        

        return Result.success(page.getTotal(), page.getPages(), response);
    }

    @PostMapping("/coupon")
    @ApiOperation("优惠券添加")
    public Result<String> coupon(@RequestBody @Valid CouponRequest couponRequest) {

        couponService.createByMerchantMoneyOff(MoneyUtils.yuanChangeFen(couponRequest.getWaterLine()),
                MoneyUtils.yuanChangeFen(couponRequest.getDiscounts()));

        return Result.success();
    }

    @DeleteMapping("/coupon/{id}")
    @ApiOperation(value = "优惠券-删除")
    public Result<String> coupon(@PathVariable long id) {

        couponService.delete(id);

        return Result.success();
    }
}
