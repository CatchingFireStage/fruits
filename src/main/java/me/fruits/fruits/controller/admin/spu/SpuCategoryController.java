package me.fruits.fruits.controller.admin.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddCategoryRequest;
import me.fruits.fruits.mapper.po.SpuCategory;
import me.fruits.fruits.service.spu.SpuCategoryAdminModuleService;
import me.fruits.fruits.service.spu.SpuCategoryService;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/spu")
@RestController(value = "AdminSpuCategoryController")
@Api(tags = "SPU-分类")
@AdminLogin
@Validated
@Slf4j
public class SpuCategoryController {


    @Autowired
    private SpuCategoryAdminModuleService spuCategoryAdminModuleService;

    @Autowired
    private SpuCategoryService spuCategoryService;

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

        spuCategories.getRecords().forEach(spuCategory -> {
            log.info("创建时间{}", spuCategory.getCreateTime().atZone(ZoneId.systemDefault()));
        });

        return Result.success(spuCategories.getTotal(), spuCategories.getPages(), spuCategories.getRecords());
    }

    @GetMapping(value = "/categoriesSearch")
    @ApiOperation("商品分类-搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "商品分类搜索名", example = "夏日倾情", required = true)
    })
    public Result<List<SpuCategory>> search(@RequestParam String keyword) throws IOException {

        List<SpuCategory> spuCategories = this.spuCategoryAdminModuleService.getSpuCategories(keyword);
        return Result.success(spuCategories);

    }

    @PostMapping(value = "/category")
    @ApiOperation("商品分类-添加")
    public Result<String> category(@RequestBody @Valid AddCategoryRequest addCategoryRequest) {
        spuCategoryService.add(addCategoryRequest.getName());
        return Result.success();
    }

    @PutMapping(value = "/category/{id}")
    @ApiOperation("商品分类-更新")
    public Result<String> category(@PathVariable long id, @RequestBody @Valid AddCategoryRequest addCategoryRequest) {
        spuCategoryService.update(id, addCategoryRequest.getName());
        return Result.success();
    }


    @DeleteMapping(value = "/category/{id}")
    @ApiOperation("商品分类-删除")
    public Result<String> category(@PathVariable long id) {
        spuCategoryService.delete(id);
        return Result.success();
    }


}
