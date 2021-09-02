package me.fruits.fruits.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.UserMapper;
import me.fruits.fruits.mapper.po.User;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAdminModuleService {

    @Autowired
    private UserMapper userMapper;


    public IPage<User> getUsers(String keyword, PageVo pageVo) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like("phone",keyword);
        }


        return userMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }
}
