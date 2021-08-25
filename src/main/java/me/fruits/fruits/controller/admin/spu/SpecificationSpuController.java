package me.fruits.fruits.controller.admin.spu;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpecificationSpuRequest;
import me.fruits.fruits.mapper.po.SpecificationSpu;
import me.fruits.fruits.service.spu.SpecificationAdminModuleSpuService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
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
    private SpecificationAdminModuleSpuService specificationAdminModuleSpuService;

    @PostMapping("/specificationSpu")
    @ApiOperation("规格与spu-关联绑定")
    public Result<String> specificationSpu(@RequestBody @Valid AddSpecificationSpuRequest addSpecificationSpuRequest) {

        SpecificationSpu specificationSpu = new SpecificationSpu();
        BeanUtils.copyProperties(addSpecificationSpuRequest, specificationSpu);
        specificationAdminModuleSpuService.add(specificationSpu);

        return Result.success();
    }


    @PutMapping("/specificationSpu/{id}")
    @ApiOperation("规格与spu-改变是否必填")
    public Result<String> specificationSpu(@PathVariable long id, @RequestBody @Valid AddSpecificationSpuRequest addSpecificationSpuRequest) {

        SpecificationSpu specificationSpu = new SpecificationSpu();
        BeanUtils.copyProperties(addSpecificationSpuRequest, specificationSpu);

        specificationAdminModuleSpuService.update(id, specificationSpu.getRequired());

        return Result.success();
    }


    @DeleteMapping("/specificationSpu/{id}")
    @ApiOperation("规格与spu-关联解除")
    public Result<String> specificationSpu(@PathVariable long id) {

        specificationAdminModuleSpuService.delete(id);

        return Result.success();
    }
}
