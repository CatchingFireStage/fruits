package me.fruits.fruits.service.user;

import me.fruits.fruits.mapper.UserMapper;
import me.fruits.fruits.mapper.po.User;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getUser(long id) {
        return this.userMapper.selectById(id);
    }

    public void add(User user) {

        this.userMapper.insert(user);
    }
}
