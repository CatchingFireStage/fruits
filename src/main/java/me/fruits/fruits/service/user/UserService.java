package me.fruits.fruits.service.user;

import me.fruits.fruits.mapper.UserMapper;
import me.fruits.fruits.mapper.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getUser(long id) {
        return this.userMapper.selectById(id);
    }

    public long add(User user) {

        this.userMapper.insert(user);

        return user.getId();
    }
}
