package me.fruits.fruits.controller.admin.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpuRequest;
import me.fruits.fruits.controller.admin.spu.vo.ChangeIsInventoryRequest;
import me.fruits.fruits.mapper.enums.IsInventoryEnum;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.spu.SpuAdminModuleService;
import me.fruits.fruits.service.spu.WrapperDTOService;
import me.fruits.fruits.service.spu.dto.SpuDTO;
import me.fruits.fruits.service.upload.UploadService;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.MoneyUtils;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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
    private SpuAdminModuleService spuAdminModuleService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private WrapperDTOService wrapperDTOService;


    @GetMapping(value = "/spu")
    @ApiOperation("列表页-spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "spu商品名搜索", example = "")
    })
    public Result<List<SpuDTO>> spu(
            @RequestParam(defaultValue = "") String keyword,
            PageVo pageVo
    ) {
        IPage<Spu> spUs = spuAdminModuleService.getSPUs(pageVo.getP(), pageVo.getPageSize(), keyword);


        if (spUs.getRecords().size() == 0) {
            return Result.success(0, 0, null);
        }

        List<SpuDTO> spuDTOS = wrapperDTOService.wrapperSPUs(spUs.getRecords());

        return Result.success(spUs.getTotal(), spUs.getPages(), spuDTOS);
    }

    @PostMapping(value = "/spu", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "添加-spu")
    public Result<String> spu(@Valid AddSpuRequest addSpuRequest, @RequestPart("file") MultipartFile file) throws IOException, FruitsException {
        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        //图片处理
        String path = uploadService.uploadBySpu(file);
        spu.setImage(path);

        //金额处理
        spu.setMoney(MoneyUtils.yuanChangeFen(addSpuRequest.getMoney()));

        spuAdminModuleService.add(spu);
        return Result.success();
    }

    @PutMapping("/spu/{id}")
    @ApiOperation(value = "更新-spu")
    public Result<String> spu(@PathVariable long id, @RequestBody @Valid AddSpuRequest addSpuRequest) {

        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        //金额处理
        spu.setMoney(MoneyUtils.yuanChangeFen(addSpuRequest.getMoney()));

        spuAdminModuleService.update(id, spu);

        return Result.success();
    }

    @PatchMapping("/spuChangeImg/{id}")
    @ApiOperation("更新spu的图片")
    public Result<String> spu(@PathVariable long id,@RequestPart("file") MultipartFile file) throws IOException, FruitsException {
        spuAdminModuleService.update(id,file);
        return Result.success();
    }

    @PatchMapping("/spuChangeIsInventory/{id}")
    @ApiOperation(value = "更新是否有货状态-spu")
    public Result<String> spu(@PathVariable long id, @RequestBody @Valid ChangeIsInventoryRequest changeIsInventoryRequest) {

        if (changeIsInventoryRequest.getIsInventory().equals(IsInventoryEnum.ON_INVENTORY.getValue())) {
            spuAdminModuleService.update(id, IsInventoryEnum.ON_INVENTORY);
        } else if (changeIsInventoryRequest.getIsInventory().equals(IsInventoryEnum.OFF_INVENTORY.getValue())) {
            spuAdminModuleService.update(id, IsInventoryEnum.OFF_INVENTORY);
        } else {
            return Result.failed("参数错误");
        }

        return Result.success();
    }


    @DeleteMapping("/spu/{id}")
    @ApiOperation(value = "删除-spu")
    public Result<String> spu(@PathVariable long id) {

        spuAdminModuleService.delete(id);

        return Result.success();
    }


}
