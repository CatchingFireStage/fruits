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
import me.fruits.fruits.service.spu.SpecificationService;
import me.fruits.fruits.service.spu.WrapperDTOService;
import me.fruits.fruits.service.spu.dto.SpecificationSpuDTO;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/spu")
@RestController(value = "AdminSpecificationController")
@Api(tags = "SPU-规格表")
@AdminLogin
@Validated
public class SpecificationController {

    @Autowired
    private SpecificationAdminModuleService specificationAdminModuleService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private WrapperDTOService wrapperDTOService;


    @GetMapping(value = "/specifications")
    @ApiOperation("规格-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "规格名", example = "")
    })
    public Result<List<SpecificationSpuDTO>> specifications(
            @RequestParam(required = false) String keyword,
            PageVo pageVo
    ) {

        IPage<Specification> specifications = specificationAdminModuleService.getSpecifications(keyword, pageVo);

        if (specifications.getRecords().size() == 0) {
            return Result.success(specifications.getTotal(), specifications.getPages(), null);
        }

        List<SpecificationSpuDTO> response = wrapperDTOService.wrapperSpecifications(specifications.getRecords());

        return Result.success(specifications.getTotal(), specifications.getPages(), response);
    }

    @PostMapping(value = "/specification")
    @ApiOperation("规格-新增")
    public Result<String> specification(@RequestBody @Valid AddSpecificationRequest addSpecificationRequest) {
        specificationService.add(addSpecificationRequest.getName());
        return Result.success();
    }

    @PutMapping("/specification/{id}")
    @ApiOperation("规格-修改")
    public Result<String> specification(@PathVariable long id, @RequestBody @Valid AddSpecificationRequest addSpecificationRequest) {
        specificationService.update(id, addSpecificationRequest.getName());
        return Result.success();
    }

}
