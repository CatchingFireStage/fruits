package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.SpuCategoryMapper;
import me.fruits.fruits.mapper.po.SpuCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SpuCategoryAdminModuleService extends SpuCategoryService {
    @Autowired
    private SpuCategoryMapper spuCategoryMapper;


    /**
     * 列表
     */
    public IPage<SpuCategory> getSpuCategories(int p, int pageSize, String keyword) {
        QueryWrapper<SpuCategory> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like("name", keyword);
        }

        queryWrapper.orderByDesc("id");

        return this.spuCategoryMapper.selectPage(new Page<>(p, pageSize), queryWrapper);
    }

    /**
     * 添加
     */
    public void add(SpuCategory spuCategory) {
        super.add(spuCategory);
    }

    /**
     * 更新
     */
    public void update(long id, SpuCategory spuCategory) {

        UpdateWrapper<SpuCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", spuCategory.getName()).set("update_time", LocalDateTime.now());

        this.spuCategoryMapper.update(null, updateWrapper);
    }
}
