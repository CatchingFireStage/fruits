package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.fruits.fruits.mapper.SpuCategoryMapper;
import me.fruits.fruits.mapper.po.SpuCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public abstract class SpuCategoryService {
    private SpuCategoryMapper spuCategoryMapper;

    public SpuCategoryMapper getSpuCategoryMapper() {
        return spuCategoryMapper;
    }

    @Autowired
    public void setSpuCategoryMapper(SpuCategoryMapper spuCategoryMapper) {
        this.spuCategoryMapper = spuCategoryMapper;
    }


    /**
     * 获取列表
     */
    public List<SpuCategory> getSpuCategories(Set<Long> ids) {
        QueryWrapper<SpuCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        return this.spuCategoryMapper.selectList(queryWrapper);
    }

    public void add(SpuCategory spuCategory) {
        spuCategory.setCreateTime(LocalDateTime.now());
        spuCategory.setUpdateTime(LocalDateTime.now());

        this.getSpuCategoryMapper().insert(spuCategory);
    }
}
