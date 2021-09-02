package me.fruits.fruits.service.spu;

import me.fruits.fruits.mapper.po.SpecificationValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationValueAdminModuleService {

    @Autowired
    private SpecificationValueService specificationValueService;


    public void add(SpecificationValue specificationValue) {

        this.specificationValueService.add(specificationValue);
    }

    public boolean update(long id, SpecificationValue specificationValue) {

        return this.specificationValueService.update(id, specificationValue);
    }

    public boolean delete(long id) {

        return this.specificationValueService.delete(id);
    }
}
