package me.fruits.fruits.controller.admin.spu;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpecificationValueRequest;
import me.fruits.fruits.mapper.po.SpecificationValue;
import me.fruits.fruits.service.spu.SpecificationValueAdminModuleService;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/spu")
@RestController(value = "AdminSpecificationValueController")
@Api(tags = "SPU-规格表")
@AdminLogin
@Validated
public class SpecificationValueController {


    @Autowired
    private SpecificationValueAdminModuleService specificationValueAdminModuleService;

    @PostMapping(value = "/specificationValue")
    @ApiOperation("规格值-新增")
    public Result<String> specificationValue(@RequestBody @Valid AddSpecificationValueRequest addSpecificationValueRequest) {

        SpecificationValue specificationValue = new SpecificationValue();
        BeanUtils.copyProperties(addSpecificationValueRequest, specificationValue);

        specificationValueAdminModuleService.add(specificationValue);

        return Result.success();
    }

    @PutMapping("/specificationValue/{id}")
    @ApiOperation("规格值-修改")
    public Result<String> specification(@PathVariable long id, @RequestBody @Valid AddSpecificationValueRequest addSpecificationValueRequest) throws FruitsException {

        SpecificationValue specificationValue = new SpecificationValue();
        BeanUtils.copyProperties(addSpecificationValueRequest, specificationValue);

        specificationValueAdminModuleService.update(id, specificationValue);

        return Result.success();
    }

    @DeleteMapping("/specificationValue/{id}")
    @ApiOperation("规格值-删除")
    public Result<String> specification(@PathVariable long id) {

        specificationValueAdminModuleService.delete(id);

        return Result.success();
    }
}
