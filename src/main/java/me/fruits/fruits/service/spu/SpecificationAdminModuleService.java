package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpecificationMapper;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SpecificationAdminModuleService extends SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;


    public List<Specification> getSpecifications(String keyword) {
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().equals("")) {
            queryWrapper.like("name", keyword.trim());
        }

        queryWrapper.last("limit 100");


        return this.specificationMapper.selectList(queryWrapper);
    }



    public void update(long id, Specification specification) throws FruitsException {

        UpdateWrapper<Specification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", specification.getName());

        try {
            this.specificationMapper.update(null, updateWrapper);
        } catch (DuplicateKeyException e) {
            throw new FruitsException("规格名已存在");
        }
    }
}
