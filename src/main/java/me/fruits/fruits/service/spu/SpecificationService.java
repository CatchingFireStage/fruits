package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.fruits.fruits.mapper.SpecificationMapper;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public void add(Specification specification){
        specificationMapper.insert(specification);
    }


    public void update(long id, String name) {

        UpdateWrapper<Specification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", name);

        try {
            this.specificationMapper.update(null, updateWrapper);
        } catch (DuplicateKeyException e) {
            throw new FruitsRuntimeException("规格名已存在");
        }
    }


    public List<Specification> getSpecifications(Set<Long> ids){
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("id",ids);

        return this.specificationMapper.selectList(queryWrapper);
    }


    public Specification getSpecification(long id){
        return this.specificationMapper.selectById(id);
    }
}
