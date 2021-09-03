package me.fruits.fruits.service.order;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.OrdersMapper;
import me.fruits.fruits.mapper.po.Orders;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderApiModuleService {


    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 获取我的订单列表
     * @param userId 用户id
     */
    public IPage<Orders> getMyOrders(long userId, PageVo pageVo){

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);

        //用户的订单不能查看状态为已关闭的订单
        queryWrapper.ne("state",2);

        return ordersMapper.selectPage(new Page<>(pageVo.getP(),pageVo.getPageSize()),queryWrapper);
    }
}
