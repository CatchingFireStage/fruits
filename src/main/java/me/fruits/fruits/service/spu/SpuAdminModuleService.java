package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpuAdminModuleService {

    @Autowired
    private SpuService spuService;

    /**
     * spu列表
     */
    public IPage<Spu> getSPUs(int p, int pageSize, String keyword) {


        LambdaQueryChainWrapper<Spu> queryWrapper = spuService.lambdaQuery();


        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like(Spu::getName, keyword);
        }

        queryWrapper.orderByDesc(Spu::getId);

        return queryWrapper.page(new Page<>(p, pageSize));
    }
}
