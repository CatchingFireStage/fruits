package me.fruits.fruits.controller.admin.merchant;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.merchant.vo.CouponRequest;
import me.fruits.fruits.controller.admin.merchant.vo.MerchantRequest;
import me.fruits.fruits.mapper.po.Coupon;
import me.fruits.fruits.service.coupon.CouponService;
import me.fruits.fruits.utils.MoneyUtils;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RequestMapping("/merchant")
@RestController(value = "CouponController")
@Api(tags = "商家-优惠券")
@AdminLogin
@Validated
public class CouponController {

    @Autowired
    private CouponService couponService;


    @GetMapping("/coupon")
    @ApiOperation("优惠券列表")
    public Result<Coupon> coupon(PageVo pageVo) {

        IPage<Coupon> page = couponService.getCouponsByAdminManage(pageVo);

        return Result.success(page.getTotal(), page.getPages(), page.getRecords());
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
