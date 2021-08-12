package me.fruits.fruits.controller.admin.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpuRequest;
import me.fruits.fruits.controller.admin.spu.vo.ChangeIsInventoryRequest;
import me.fruits.fruits.mapper.enums.IsInventoryEnum;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.spu.SpuAdminModuleService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * GET（SELECT）：从服务器取出资源（一项或多项）。
 * <p>
 * POST（CREATE）：在服务器新建一个资源。
 * <p>
 * PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
 * <p>
 * PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
 * <p>
 * DELETE（DELETE）：从服务器删除资源。
 */
@RequestMapping("/spu")
@RestController(value = "AdminSpuController")
@Api(tags = "SPU")
@AdminLogin
@Validated
public class SpuController {

    @Autowired
    private SpuAdminModuleService adminModuleService;


    @GetMapping(value = "/spu")
    @ApiOperation("列表页-spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p", value = "第几页", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "spu商品名搜索", defaultValue = "")
    })
    public Result<HashMap<String, Object>> spu(
            @RequestParam(defaultValue = "1") Integer p,
            @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "") String keyword
    ) {
        IPage<Spu> spUs = adminModuleService.getSPUs(p, pageSize, keyword);
        return Result.success(spUs.getTotal(), spUs.getPages(), spUs.getRecords());
    }

    @PostMapping(value = "/spu")
    @ApiOperation(value = "添加-spu")
    public Result<String> spu(@RequestBody @Valid AddSpuRequest addSpuRequest) {

        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        adminModuleService.add(spu);
        return Result.success("成功");
    }

    @PutMapping("/spu/{id}")
    @ApiOperation(value = "更新-spu")
    public Result<String> spu(@PathVariable long id, @RequestBody @Valid AddSpuRequest addSpuRequest) {

        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        adminModuleService.update(id, spu);

        return Result.success("成功");
    }

    @PatchMapping("/spu/{id}")
    @ApiOperation(value = "更新是否有货状态-spu")
    public Result<String> spu(@PathVariable long id, @RequestBody @Valid ChangeIsInventoryRequest changeIsInventoryRequest) {

        if (changeIsInventoryRequest.getIsInventory().equals(IsInventoryEnum.ON_INVENTORY.getValue())) {
            adminModuleService.update(id, IsInventoryEnum.ON_INVENTORY);
        } else if (changeIsInventoryRequest.getIsInventory().equals(IsInventoryEnum.OFF_INVENTORY.getValue())) {
            adminModuleService.update(id, IsInventoryEnum.OFF_INVENTORY);
        } else {
            return Result.failed("参数错误");
        }

        return Result.success("成功");
    }


    @DeleteMapping("/spu/{id}")
    @ApiOperation(value = "删除-spu")
    public Result<String> spu(@PathVariable long id) {
        return Result.failed("暂时不支持删除");
    }


}
