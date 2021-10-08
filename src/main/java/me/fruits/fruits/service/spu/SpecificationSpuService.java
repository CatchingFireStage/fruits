package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.fruits.fruits.mapper.SpecificationSpuMapper;
import me.fruits.fruits.mapper.po.SpecificationSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SpecificationSpuService {

    @Autowired
    private SpecificationSpuMapper specificationSpuMapper;

    /**
     * 通过spuid，获取spu必选的规格
     */
    public List<SpecificationSpu> getSpecificationSpuRequiredBySpuId(long spuId) {

        QueryWrapper<SpecificationSpu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        queryWrapper.eq("required", 1);

        return this.specificationSpuMapper.selectList(queryWrapper);
    }

    /**
     * 通过spuId,获取spu所关联的所有规格
     */
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


    public void update(long id, int required) {

        UpdateWrapper<SpecificationSpu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("required", required);

        this.specificationSpuMapper.update(null, updateWrapper);

    }
}
