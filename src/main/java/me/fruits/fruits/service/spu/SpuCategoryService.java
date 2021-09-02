package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.fruits.fruits.mapper.SpuCategoryMapper;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.mapper.po.SpuCategory;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class SpuCategoryService {

    @Autowired
    private SpuCategoryMapper spuCategoryMapper;

    @Autowired
    private SpuMapper spuMapper;


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

        this.spuCategoryMapper.insert(spuCategory);
    }


    /**
     * 更新名字
     */
    public void update(long id, String name) {

        UpdateWrapper<SpuCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", name).set("update_time", LocalDateTime.now());

        this.spuCategoryMapper.update(null, updateWrapper);
    }


    /**
     * 删除
     *
     * @param id
     */
    public void delete(long id) {

        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", id);
        if (this.spuMapper.selectCount(queryWrapper) > 0) {
            throw new FruitsRuntimeException("删除分类下所有商品才能删除分类");
        }

        this.spuCategoryMapper.deleteById(id);
    }
}
