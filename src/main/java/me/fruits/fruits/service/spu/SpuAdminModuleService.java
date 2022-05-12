package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpuAdminModuleService extends ServiceImpl<SpuMapper, Spu> {


    /**
     * spu列表
     */
    public IPage<Spu> getSPUs(int p, int pageSize, String keyword) {


        LambdaQueryChainWrapper<Spu> queryWrapper = lambdaQuery();


        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like(Spu::getName, keyword);
        }

        queryWrapper.orderByDesc(Spu::getId);

        return page(new Page<>(p, pageSize), queryWrapper);
    }
}
