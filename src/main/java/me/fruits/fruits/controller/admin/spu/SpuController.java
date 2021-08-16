package me.fruits.fruits.controller.admin.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.controller.admin.spu.vo.AddSpuRequest;
import me.fruits.fruits.controller.admin.spu.vo.ChangeIsInventoryRequest;
import me.fruits.fruits.mapper.enums.IsInventoryEnum;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.mapper.po.SpuCategory;
import me.fruits.fruits.service.spu.SpuAdminModuleService;
import me.fruits.fruits.service.spu.SpuCategoryAdminModuleService;
import me.fruits.fruits.service.upload.UploadService;
import me.fruits.fruits.utils.FruitsException;
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
import java.util.stream.Collectors;

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
    private SpuCategoryAdminModuleService spuCategoryAdminModuleService;

    @Autowired
    private UploadService uploadService;


    @GetMapping(value = "/spu")
    @ApiOperation("列表页-spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "spu商品名搜索", example = "")
    })
    public Result<HashMap<String, Object>> spu(
            @RequestParam(defaultValue = "") String keyword,
            PageVo pageVo
    ) {
        IPage<Spu> spUs = spuAdminModuleService.getSPUs(pageVo.getP(), pageVo.getPageSize(), keyword);


        if (spUs.getRecords().size() == 0) {
            return Result.success(0, 0, null);
        }

        Set<Long> categoryIds = spUs.getRecords().stream().map(Spu::getCategoryId).collect(Collectors.toSet());
        Map<Long, SpuCategory> categoryMapKeyIsId = spuCategoryAdminModuleService.getSpuCategories(categoryIds).stream().collect(Collectors.toMap(SpuCategory::getId, spuCategory -> spuCategory));


        List<Object> response = new ArrayList<>();

        spUs.getRecords().forEach(spu -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", spu.getId());
            item.put("name", spu.getName());
            item.put("isInventory", spu.getIsInventory());
            item.put("image", uploadService.imageVo(spu.getImage()));
            item.put("category", categoryMapKeyIsId.getOrDefault(spu.getCategoryId(), null));

            response.add(item);
        });

        return Result.success(spUs.getTotal(), spUs.getPages(), response);
    }

    @PostMapping(value = "/spu", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "添加-spu")
    public Result<String> spu(@Valid AddSpuRequest addSpuRequest, @RequestPart("file") MultipartFile file) throws IOException, FruitsException {
        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        //图片处理
        String path = uploadService.uploadBySpu(file);
        spu.setImage(path);
        spuAdminModuleService.add(spu);
        return Result.success();
    }

    @PutMapping("/spu/{id}")
    @ApiOperation(value = "更新-spu")
    public Result<String> spu(@PathVariable long id, @RequestBody @Valid AddSpuRequest addSpuRequest) {

        Spu spu = new Spu();
        BeanUtils.copyProperties(addSpuRequest, spu);

        spuAdminModuleService.update(id, spu);

        return Result.success();
    }

    @PatchMapping("/spu/{id}")
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
