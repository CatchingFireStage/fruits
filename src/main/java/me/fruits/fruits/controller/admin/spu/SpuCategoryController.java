package me.fruits.fruits.controller.admin.spu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddCategoryRequest;
import me.fruits.fruits.utils.Result;
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

    @GetMapping(value = "/categories")
    @ApiOperation("商品分类-列表页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p", value = "第几页", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", defaultValue = "50"),
            @ApiImplicitParam(name = "keyword", value = "商品分类搜索名", defaultValue = "")
    })
    public Result<HashMap<String, Object>> categories(
            @RequestParam(defaultValue = "1") Integer p,
            @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "") String keyword
    ) {
        //todo 列表页

        return Result.success(0, 0, null);
    }

    @PostMapping(value = "/category")
    @ApiOperation("商品分类-添加")
    public Result<String> category(@RequestBody @Valid AddCategoryRequest addCategoryRequest) {

        return Result.success("成功");
    }

    @PutMapping(value = "/category/{id}")
    @ApiOperation("商品分类-更新")
    public Result<String> category(@PathVariable long id, @RequestBody @Valid AddCategoryRequest addCategoryRequest) {

        return Result.success("成功");
    }


}
