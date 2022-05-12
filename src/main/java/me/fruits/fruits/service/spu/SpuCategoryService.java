package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.SpuCategoryMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.mapper.po.SpuCategory;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class SpuCategoryService extends ServiceImpl<SpuCategoryMapper, SpuCategory> {


    @Autowired
    private SpuService spuService;


    /**
     * 获取列表
     */
    public List<SpuCategory> getSpuCategories(Set<Long> ids) {
        return lambdaQuery().in(SpuCategory::getId, ids)
                .list();
    }

    public void add(String name) {

        SpuCategory spuCategory = new SpuCategory();
        spuCategory.setName(name);
        spuCategory.setCreateTime(LocalDateTime.now());
        spuCategory.setUpdateTime(LocalDateTime.now());
        save(spuCategory);
    }


    /**
     * 更新名字
     */
    public void update(long id, String name) {

        lambdaUpdate().eq(SpuCategory::getId, id)
                .set(SpuCategory::getName, name)
                .set(SpuCategory::getUpdateTime, LocalDateTime.now())
                .update();
    }


    /**
     * 删除
     *
     * @param id
     */
    public void delete(long id) {

        Long count = spuService.lambdaQuery().eq(Spu::getCategoryId, id).count();
        if (count > 0) {
            throw new FruitsRuntimeException("删除分类下所有商品才能删除分类");
        }

        removeById(id);
    }
}
