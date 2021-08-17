package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.fruits.fruits.mapper.SpecificationSpuMapper;
import me.fruits.fruits.mapper.po.SpecificationSpu;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public abstract class SpecificationSpuService {

    @Autowired
    private SpecificationSpuMapper specificationSpuMapper;


    public List<SpecificationSpu> getSpecificationSpuBySpuId(long spuId) {
        QueryWrapper<SpecificationSpu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);

        return this.specificationSpuMapper.selectList(queryWrapper);
    }

    public List<SpecificationSpu> getSpecificationSpuBySpuId(Set<Long> spuIds) {
        QueryWrapper<SpecificationSpu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("spu_id", spuIds);
        return this.specificationSpuMapper.selectList(queryWrapper);
    }


    public void add(SpecificationSpu specificationSpu) {
        specificationSpuMapper.insert(specificationSpu);
    }


    public void delete(long id) {
        specificationSpuMapper.deleteById(id);
    }
}
