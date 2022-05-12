package me.fruits.fruits.service.spu;

import me.fruits.fruits.mapper.po.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuApiModuleService {


    @Autowired
    private SpuService spuService;

    /**
     * 经典菜单的数据获取
     */
    public List<Spu> getClassicMenu() {
        return spuService.lambdaQuery().eq(Spu::getIsInventory, true).list();
    }
}
