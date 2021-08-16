package me.fruits.fruits.controller.admin.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpecificationRequest;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.service.spu.SpecificationAdminModuleService;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/spu")
@RestController(value = "AdminSpecificationController")
@Api(tags = "SPU-规格表")
@AdminLogin
@Validated
public class SpecificationController {

    @Autowired
    private SpecificationAdminModuleService specificationAdminModuleService;


    @GetMapping(value = "/specifications")
    @ApiOperation("规格-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "规格名", example = "")
    })
    public Result<HashMap<String, Object>> specifications(
            @RequestParam(required = false) String keyword,
            PageVo pageVo
    ) {

        IPage<Specification> specifications = specificationAdminModuleService.getSpecifications(keyword, pageVo);

        return Result.success(specifications.getTotal(),specifications.getPages(),specifications.getRecords());
    }


    @GetMapping(value = "/search")
    @ApiOperation("规格-搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "规格名")
    })
    public Result<List<Specification>> search(@RequestParam String keyword) throws IOException {

        return Result.success(specificationAdminModuleService.getSpecifications(keyword));
    }


    @PostMapping(value = "/specification")
    @ApiOperation("规格-新增")
    public Result<String> specification(@RequestBody @Valid AddSpecificationRequest addSpecificationRequest) {


        Specification specification = new Specification();
        BeanUtils.copyProperties(addSpecificationRequest, specification);
        specificationAdminModuleService.add(specification);
        return Result.success();
    }

    @PutMapping("/specification/{id}")
    @ApiOperation("规格-修改")
    public Result<String> specification(@PathVariable long id, @RequestBody @Valid AddSpecificationRequest addSpecificationRequest) throws FruitsException {

        Specification specification = new Specification();
        BeanUtils.copyProperties(addSpecificationRequest, specification);

        specificationAdminModuleService.update(id, specification);
        return Result.success();
    }

}
