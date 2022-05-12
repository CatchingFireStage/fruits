package me.fruits.fruits.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.UserMapper;
import me.fruits.fruits.mapper.po.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {


    public User getUser(long id) {
        return getById(id);
    }

    public long add(String phone) {

        User user = new User();

        user.setPhone(phone);

        save(user);

        return user.getId();
    }
}
