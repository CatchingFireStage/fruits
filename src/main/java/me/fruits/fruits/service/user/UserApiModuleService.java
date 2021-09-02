package me.fruits.fruits.service.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApiModuleService {

    @Autowired
    private UserService userService;

}
