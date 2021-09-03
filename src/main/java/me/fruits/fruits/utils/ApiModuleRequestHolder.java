package me.fruits.fruits.utils;

import me.fruits.fruits.service.api.UserDTO;
import org.springframework.util.ObjectUtils;

public class ApiModuleRequestHolder {

    private final static ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void set(UserDTO userDTO) {
        threadLocal.set(userDTO);
    }

    public static UserDTO get() {
        UserDTO userDTO = threadLocal.get();
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new FruitsRuntimeException("获取不到当前登录的用户");
        }
        return userDTO;
    }
}
