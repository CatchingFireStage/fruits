package me.fruits.fruits.service.spu;

import me.fruits.fruits.mapper.SpuCategoryMapper;
import me.fruits.fruits.mapper.po.SpuCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public abstract class SpuCategoryService {
    private SpuCategoryMapper spuCategoryMapper;

    public SpuCategoryMapper getSpuCategoryMapper() {
        return spuCategoryMapper;
    }

    @Autowired
    public void setSpuCategoryMapper(SpuCategoryMapper spuCategoryMapper) {
        this.spuCategoryMapper = spuCategoryMapper;
    }


    public void add(SpuCategory spuCategory) {
        spuCategory.setCreateTime(LocalDateTime.now());
        spuCategory.setUpdateTime(LocalDateTime.now());

        this.getSpuCategoryMapper().insert(spuCategory);
    }
}
