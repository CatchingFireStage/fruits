package me.fruits.fruits.service.spu;

import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * 公共的服务
 */
abstract public class SpuService {
    private SpuMapper spuMapper;

    public SpuMapper getSpuMapper() {
        return spuMapper;
    }

    @Autowired
    public void setSpuMapper(SpuMapper spuMapper) {
        this.spuMapper = spuMapper;
    }

    /**
     * 添加一个spu
     */
    public void add(Spu spu) {

        spu.setCreateTime(LocalDateTime.now());
        spu.setUpdateTime(LocalDateTime.now());
        this.getSpuMapper().insert(spu);
    }
}
