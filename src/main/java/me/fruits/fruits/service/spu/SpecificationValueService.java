package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.fruits.fruits.mapper.SpecificationValueMapper;
import me.fruits.fruits.mapper.po.SpecificationValue;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SpecificationValueService {

    @Autowired
    private SpecificationValueMapper specificationValueMapper;

    public void add(SpecificationValue specificationValue) {

        this.specificationValueMapper.insert(specificationValue);
    }

    public boolean update(long id, SpecificationValue specificationValue) {
        UpdateWrapper<SpecificationValue> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("id", id);
        updateWrapper.set("specification_id", specificationValue.getSpecificationId())
                .set("value", specificationValue.getValue());

        return this.specificationValueMapper.update(null, updateWrapper) > 0;
    }

    public boolean delete(long id) {

        return this.specificationValueMapper.deleteById(id) > 0;
    }
}
