package me.fruits.fruits.controller.admin.merchant;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.merchant.vo.MerchantRequest;
import me.fruits.fruits.service.merchant.MerchantAdminModuleService;
import me.fruits.fruits.service.merchant.MerchantService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/merchant")
@RestController(value = "AdminMerchantController")
@Api(tags = "商家")
@AdminLogin
@Validated
public class MerchantController {

    @Autowired
    private MerchantAdminModuleService merchantAdminModuleService;


    @GetMapping("/merchant")
    @ApiOperation("商家信息-详情")
    public Result<MerchantService.MerchantDTO> merchant(){

        MerchantService.MerchantDTO merchant = merchantAdminModuleService.getMerchant();

        return Result.success(merchant);
    }

    @PostMapping("/merchant")
    @ApiOperation("商家信息-修改")
    public Result<String> merchant(@RequestBody @Valid MerchantRequest merchantRequest) {

        merchantAdminModuleService.setStartTime(merchantRequest.getStartTime());
        merchantAdminModuleService.setEndTime(merchantRequest.getEndTime());
        merchantAdminModuleService.setIs24Hours(merchantRequest.getIs24Hours());
        merchantAdminModuleService.setIsClose(merchantRequest.getIsClose());


        return Result.success();
    }
}
