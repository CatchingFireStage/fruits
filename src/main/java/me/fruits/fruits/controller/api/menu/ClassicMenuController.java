package me.fruits.fruits.controller.api.menu;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.configuration.CacheConfiguration;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.spu.SpuApiModuleService;
import me.fruits.fruits.service.spu.WrapperDTOService;
import me.fruits.fruits.service.spu.dto.SpuCategoryDTO;
import me.fruits.fruits.service.spu.dto.SpuDTO;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/menu")
@RestController(value = "apiClassicMenuController")
@Api(tags = "菜单-经典菜单")
@Validated
public class ClassicMenuController {

    @Autowired
    private SpuApiModuleService spuApiModuleService;

    @Autowired
    private WrapperDTOService wrapperDTOService;


    @ApiOperation(value = "经典菜单")
    @GetMapping("/menu")
    @Cacheable(value = CacheConfiguration.menuCacheManager_cache_menu, cacheManager = CacheConfiguration.menuCacheManager)
    public Result<Object> menu() {

        //获取商品
        List<Spu> spus = spuApiModuleService.getClassicMenu();


        List<SpuDTO> spuDTOS = wrapperDTOService.wrapperSPUs(spus);

        //获取类别，去重
        Map<Long, SpuCategoryDTO> categoryMapKeyIsCategoryId = spuDTOS.stream().map(SpuDTO::getCategory).collect(Collectors.toMap(
                SpuCategoryDTO::getId, spuCategoryDTO -> spuCategoryDTO,
                //如果key重复，取后面的值覆盖
                (last, next) -> next
        ));

        //菜单左边的分类项
        List<SpuCategoryDTO> category = new ArrayList<>();

        categoryMapKeyIsCategoryId.forEach((key, value) -> {


            //分类
            category.add(value);

        });


        //商品按照类别分组
        Map<Long, List<SpuDTO>> goodsMapKeyIsCategoryId = spuDTOS.stream().collect(Collectors.groupingBy(spuDTO -> spuDTO.getCategory().getId()));

        List<Object> goods = new ArrayList<>();

        categoryMapKeyIsCategoryId.forEach((key, value) -> {


            Map<String, Object> goodsItem = new HashMap<>();

            goodsItem.put("list", goodsMapKeyIsCategoryId.get(value.getId()));


            //分类
            Map<String, Object> categoryItem = new HashMap<>();
            categoryItem.put("id", value.getId());
            categoryItem.put("name", value.getName());

            goodsItem.put("category", categoryItem);


            goods.add(goodsItem);
        });


        Map<String, Object> response = new HashMap<>();

        response.put("category", category);

        response.put("goods", goods);


        return Result.success(response);
    }
}
