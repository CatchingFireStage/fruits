package me.fruits.fruits.service.spu;

import me.fruits.fruits.mapper.SpecificationMapper;
import me.fruits.fruits.mapper.po.Specification;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public void add(Specification specification){
        specificationMapper.insert(specification);
    }
}
