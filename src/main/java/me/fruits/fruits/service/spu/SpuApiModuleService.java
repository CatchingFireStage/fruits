package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuApiModuleService {

    @Autowired
    private SpuMapper spuMapper;


    /**
     * 经典菜单的数据获取
     */
    public List<Spu> getClassicMenu() {
        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        //有货
        queryWrapper.eq("is_inventory", 1);
        return spuMapper.selectList(queryWrapper);
    }
}
