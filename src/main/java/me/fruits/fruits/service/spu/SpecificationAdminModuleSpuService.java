package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.fruits.fruits.mapper.SpecificationSpuMapper;
import me.fruits.fruits.mapper.po.SpecificationSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationAdminModuleSpuService extends SpecificationSpuService {

    @Autowired
    private SpecificationSpuMapper specificationSpuMapper;


    public void update(long id, int required) {

        UpdateWrapper<SpecificationSpu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("required", required);

        this.specificationSpuMapper.update(null, updateWrapper);

    }
}
