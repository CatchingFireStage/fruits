package me.fruits.fruits.controller.api.menu;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.ApiLogin;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.spu.WrapperDTOService;
import me.fruits.fruits.service.spu.dto.SpuCategoryDTO;
import me.fruits.fruits.service.spu.dto.SpuDTO;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
@ApiLogin
public class ClassicMenuController {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private WrapperDTOService wrapperDTOService;


    @ApiOperation(value = "经典菜单")
    @GetMapping("/menu")
    public Result<List<Object>> menu() {


        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        //有货
        queryWrapper.eq("is_inventory", 1);
        List<Spu> spus = spuMapper.selectList(queryWrapper);


        List<SpuDTO> spuDTOS = wrapperDTOService.wrapperSPUs(spus);

        //获取所有类别
        Map<Long, SpuCategoryDTO> categoryMapKeyIsCategoryId = spuDTOS.stream().map(SpuDTO::getCategory).collect(Collectors.toMap(
                SpuCategoryDTO::getId, spuCategoryDTO -> spuCategoryDTO,
                //如果key重复，取后面的值覆盖
                (last,next) -> next
                ));

        //获取spu数据，然后按照类别分组
        Map<Long, List<SpuDTO>> goodsMapKeyIsCategoryId = spuDTOS.stream().collect(Collectors.groupingBy(spu -> {
            return spu.getCategory().getId();
        }));


        List<Object> response = new ArrayList<>();
        categoryMapKeyIsCategoryId.forEach((key,value) -> {

            Map<String,Object> item = new HashMap<>();
            //类别数据
            item.put("id",value.getId());
            item.put("name",value.getName());

            //类别下的所有商品
            item.put("goods",goodsMapKeyIsCategoryId.get(value.getId()));


            response.add(item);
        });

        return Result.success(response);
    }
}
