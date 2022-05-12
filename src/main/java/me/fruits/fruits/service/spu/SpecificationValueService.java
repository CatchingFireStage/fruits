package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.SpecificationValueMapper;
import me.fruits.fruits.mapper.po.SpecificationValue;
import me.fruits.fruits.utils.MoneyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;

@Service
public class SpecificationValueService extends ServiceImpl<SpecificationValueMapper, SpecificationValue> {



    /**
     * @param specificationId 规格id
     * @param value           规格值
     * @param money           价格，单位元
     */
    public void add(long specificationId, String value, @Nullable String money) {

        SpecificationValue specificationValue = new SpecificationValue();
        specificationValue.setSpecificationId(specificationId);
        specificationValue.setValue(value);
        if (money != null) {
            specificationValue.setMoney(MoneyUtils.yuanChangeFen(money));
        }

        save(specificationValue);
    }


    /**
     * @param money 单位元
     */
    public boolean update(long id, long specificationId, String value, @Nullable String money) {

        LambdaUpdateChainWrapper<SpecificationValue> updateChainWrapper = lambdaUpdate().eq(SpecificationValue::getId, id)
                .set(SpecificationValue::getSpecificationId, specificationId)
                .set(SpecificationValue::getValue, value);

        if (money != null) {
            updateChainWrapper.set(SpecificationValue::getMoney, MoneyUtils.yuanChangeFen(money));
        }

        return updateChainWrapper.update();
    }

    public boolean delete(long id) {
        return removeById(id);
    }

    public List<SpecificationValue> getSpecificationValuesBySpecificationId(Set<Long> specificationIds) {
        return lambdaQuery().in(SpecificationValue::getSpecificationId, specificationIds).list();
    }

    public SpecificationValue getSpecificationValue(long id) {
        return lambdaQuery().eq(SpecificationValue::getId, id).one();
    }
}
