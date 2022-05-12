package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuApiModuleService extends ServiceImpl<SpuMapper, Spu> {


    /**
     * 经典菜单的数据获取
     */
    public List<Spu> getClassicMenu() {
        return lambdaQuery().eq(Spu::getIsInventory, true).list();
    }
}
