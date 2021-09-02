package me.fruits.fruits.service.spu;

import me.fruits.fruits.mapper.po.SpecificationSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationAdminModuleSpuService {


    @Autowired
    private SpecificationSpuService specificationSpuService;


    public void add(SpecificationSpu specificationSpu) {


        this.specificationSpuService.add(specificationSpu);

    }


    public void delete(long id) {
        this.specificationSpuService.delete(id);
    }

    public void update(long id, int required) {

        this.specificationSpuService.update(id, required);
    }
}
