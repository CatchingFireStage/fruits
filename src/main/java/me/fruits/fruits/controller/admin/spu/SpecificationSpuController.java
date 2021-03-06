package me.fruits.fruits.controller.admin.spu;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpecificationSpuRequest;
import me.fruits.fruits.controller.admin.spu.vo.ChangeSpecificationSpuRequiredRequest;
import me.fruits.fruits.service.spu.SpecificationSpuService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/spu")
@RestController(value = "AdminSpecificationSpuController")
@Api(tags = "SPU-规格表-关联")
@AdminLogin
@Validated
public class SpecificationSpuController {


    @Autowired
    private SpecificationSpuService specificationSpuService;

    @PostMapping("/specificationSpu")
    @ApiOperation("规格与spu-关联绑定")
    public Result<String> specificationSpu(@RequestBody @Valid AddSpecificationSpuRequest addSpecificationSpuRequest) {

        specificationSpuService.add(addSpecificationSpuRequest.getSpuId(), addSpecificationSpuRequest.getSpecificationId(),
                addSpecificationSpuRequest.getRequired());

        return Result.success();
    }


    @PutMapping("/specificationSpu/{id}")
    @ApiOperation("规格与spu-改变是否必填")
    public Result<String> specificationSpu(@PathVariable long id, @RequestBody @Valid ChangeSpecificationSpuRequiredRequest changeSpecificationSpuRequiredRequest) {


        specificationSpuService.update(id, changeSpecificationSpuRequiredRequest.getRequired());

        return Result.success();
    }


    @DeleteMapping("/specificationSpu/{id}")
    @ApiOperation("规格与spu-关联解除")
    public Result<String> specificationSpu(@PathVariable long id) {

        specificationSpuService.delete(id);

        return Result.success();
    }
}
