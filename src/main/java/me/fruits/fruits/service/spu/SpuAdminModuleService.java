package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.enums.IsInventoryEnum;
import me.fruits.fruits.mapper.po.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SpuAdminModuleService extends SpuService {

    @Autowired
    private SpuMapper spuMapper;

    /**
     * spu列表
     */
    public IPage<Spu> getSPUs(int p, int pageSize, String keyword) {

        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();

        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like("name", keyword);
        }

        queryWrapper.orderByDesc("id");

        return this.spuMapper.selectPage(new Page<>(p, pageSize), queryWrapper);
    }

    /**
     * 添加一个spu
     */
    public void add(Spu spu) {
        super.add(spu);
    }

    /**
     * 更新spu的分类、是否有有货
     */
    public void update(long id, Spu spu) {
        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("category_id", spu.getCategoryId())
                .set("is_inventory", spu.getIsInventory())
                .set("update_time", LocalDateTime.now());
        this.spuMapper.update(null, updateWrapper);
    }

    /**
     * 改变是否有货
     */
    public void update(long id, IsInventoryEnum isInventoryEnum) {

        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("is_inventory", isInventoryEnum.getValue());
        this.spuMapper.update(null, updateWrapper);
    }
}
