package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.SpecificationMapper;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpecificationService extends ServiceImpl<SpecificationMapper, Specification> {


    public void add(String name) {

        Specification specification = new Specification();
        specification.setName(name);

        save(specification);
    }


    public void update(long id, String name) {

        try {
            lambdaUpdate().eq(Specification::getId, id)
                    .set(Specification::getName, name)
                    .update();
        } catch (DuplicateKeyException e) {
            throw new FruitsRuntimeException("规格名已存在");
        }
    }


    public List<Specification> getSpecifications(Set<Long> ids) {
        return lambdaQuery().in(Specification::getId, ids).list();
    }


    public Specification getSpecification(long id) {
        return getById(id);
    }
}
