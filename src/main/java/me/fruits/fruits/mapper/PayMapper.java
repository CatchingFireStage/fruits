package me.fruits.fruits.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.fruits.fruits.mapper.po.Pay;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PayMapper extends BaseMapper<Pay> {

    /**
     * 累加本次退款金额
     *
     * @param id     支付id
     * @param amount 本次退款的金额
     */
    @Update({
            "UPDATE pay",
            "SET amount = amount + #{amount}",
            "WHERE",
            "id = #{id}"
    })
    public Integer accumulationRefundAmount(Long id, Integer amount);
}
