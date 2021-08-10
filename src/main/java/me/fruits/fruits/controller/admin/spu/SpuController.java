package me.fruits.fruits.controller.admin.spu;

import io.swagger.annotations.Api;
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

    @PostMapping(value = "/spu")
    @ApiOperation(value = "添加一个spu")
    public Result<String> spu(@RequestBody @Valid AddSpuRequest addSpuRequest) {

        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        adminModuleService.add(spu);
        return Result.success("成功");
    }

    @PutMapping("/spu/{id}")
    @ApiOperation(value = "更新一个spu")
    public Result<String> spu(@PathVariable long id, @RequestBody @Valid AddSpuRequest addSpuRequest) {

        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        adminModuleService.update(id, spu);

        return Result.success("成功");
    }

    @PatchMapping("/spu/{id}")
    @ApiOperation(value = "更新是否有货状态")
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
    @ApiOperation(value = "删除一个spu")
    public Result<String> spu(@PathVariable long id) {
        return Result.failed("暂时不支持删除");
    }


}
