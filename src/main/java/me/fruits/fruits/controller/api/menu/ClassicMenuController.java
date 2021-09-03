package me.fruits.fruits.controller.api.menu;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.ApiLogin;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.spu.WrapperDTOService;
import me.fruits.fruits.service.spu.dto.SpuDTO;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Result<List<SpuDTO>> menu(){


        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        //有货
        queryWrapper.eq("is_inventory",1);
        List<Spu> spus = spuMapper.selectList(queryWrapper);

        List<SpuDTO> spuDTOS = wrapperDTOService.wrapperSPUs(spus);

        return Result.success(spuDTOS);
    }
}
