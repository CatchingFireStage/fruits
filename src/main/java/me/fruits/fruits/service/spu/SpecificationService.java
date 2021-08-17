package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.fruits.fruits.mapper.SpecificationMapper;
import me.fruits.fruits.mapper.po.Specification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

abstract public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public void add(Specification specification){
        specificationMapper.insert(specification);
    }


    public List<Specification> getSpecifications(Set<Long> ids){
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("id",ids);

        return this.specificationMapper.selectList(queryWrapper);
    }
}
