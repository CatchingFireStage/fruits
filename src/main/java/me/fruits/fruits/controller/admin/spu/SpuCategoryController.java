package me.fruits.fruits.controller.admin.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddCategoryRequest;
import me.fruits.fruits.mapper.po.SpuCategory;
import me.fruits.fruits.service.spu.SpuCategoryAdminModuleService;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RequestMapping("/spu")
@RestController(value = "AdminSpuCategoryController")
@Api(tags = "SPU")
@AdminLogin
@Validated
public class SpuCategoryController {


    @Autowired
    private SpuCategoryAdminModuleService spuCategoryAdminModuleService;

    @GetMapping(value = "/categories")
    @ApiOperation("商品分类-列表页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "商品分类搜索名", example = "")
    })
    public Result<HashMap<String, Object>> categories(
            @RequestParam(defaultValue = "") String keyword,
            PageVo pageVo
    ) {
        
        IPage<SpuCategory> spuCategories = this.spuCategoryAdminModuleService.getSpuCategories(pageVo.getP(), pageVo.getPageSize(), keyword);

        return Result.success(spuCategories.getTotal(), spuCategories.getPages(), spuCategories.getRecords());
    }

    @PostMapping(value = "/category")
    @ApiOperation("商品分类-添加")
    public Result<String> category(@RequestBody @Valid AddCategoryRequest addCategoryRequest) {

        SpuCategory spuCategory = new SpuCategory();
        BeanUtils.copyProperties(addCategoryRequest, spuCategory);
        spuCategoryAdminModuleService.add(spuCategory);
        return Result.success("成功");
    }

    @PutMapping(value = "/category/{id}")
    @ApiOperation("商品分类-更新")
    public Result<String> category(@PathVariable long id, @RequestBody @Valid AddCategoryRequest addCategoryRequest) {

        SpuCategory spuCategory = new SpuCategory();
        BeanUtils.copyProperties(addCategoryRequest, spuCategory);
        spuCategoryAdminModuleService.update(id, spuCategory);
        return Result.success("成功");
    }


}
