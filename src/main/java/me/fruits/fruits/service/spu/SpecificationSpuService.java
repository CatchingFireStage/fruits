package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.SpecificationSpuMapper;
import me.fruits.fruits.mapper.po.SpecificationSpu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SpecificationSpuService extends ServiceImpl<SpecificationSpuMapper, SpecificationSpu> {


    /**
     * 通过spuid，获取spu必选的规格
     */
    public List<SpecificationSpu> getSpecificationSpuRequiredBySpuId(long spuId) {

        return lambdaQuery().eq(SpecificationSpu::getSpuId, spuId)
                .eq(SpecificationSpu::getRequired, 1).list();
    }

    /**
     * 通过spuId,获取spu所关联的所有规格
     */
    public List<SpecificationSpu> getSpecificationSpuBySpuId(long spuId) {

        return lambdaQuery().eq(SpecificationSpu::getSpuId, spuId).list();
    }

    public List<SpecificationSpu> getSpecificationSpuBySpuId(Set<Long> spuIds) {
        return lambdaQuery().in(SpecificationSpu::getSpuId, spuIds).list();
    }


    public void add(long spuId, long specificationId, int required) {
        SpecificationSpu specificationSpu = new SpecificationSpu();
        specificationSpu.setSpuId(spuId);
        specificationSpu.setSpecificationId(specificationId);
        specificationSpu.setRequired(required);

        save(specificationSpu);
    }


    public void delete(long id) {
        removeById(id);
    }


    public void update(long id, int required) {

        lambdaUpdate().eq(SpecificationSpu::getId, id)
                .set(SpecificationSpu::getRequired, required)
                .update();

    }
}
