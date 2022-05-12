package me.fruits.fruits.controller.admin.spu;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpecificationValueRequest;
import me.fruits.fruits.service.spu.SpecificationValueService;
import me.fruits.fruits.utils.Result;
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
    private SpecificationValueService specificationValueService;

    @PostMapping(value = "/specificationValue")
    @ApiOperation("规格值-新增")
    public Result<String> specificationValue(@RequestBody @Valid AddSpecificationValueRequest addSpecificationValueRequest) {

        specificationValueService.add(addSpecificationValueRequest.getSpecificationId(), addSpecificationValueRequest.getValue(),
                addSpecificationValueRequest.getMoney());

        return Result.success();
    }

    @PutMapping("/specificationValue/{id}")
    @ApiOperation("规格值-修改")
    public Result<String> specification(@PathVariable long id, @RequestBody @Valid AddSpecificationValueRequest addSpecificationValueRequest) {


        specificationValueService.update(id, addSpecificationValueRequest.getSpecificationId(), addSpecificationValueRequest.getValue(),
                addSpecificationValueRequest.getMoney());

        return Result.success();
    }

    @DeleteMapping("/specificationValue/{id}")
    @ApiOperation("规格值-删除")
    public Result<String> specification(@PathVariable long id) {

        specificationValueService.delete(id);

        return Result.success();
    }
}
