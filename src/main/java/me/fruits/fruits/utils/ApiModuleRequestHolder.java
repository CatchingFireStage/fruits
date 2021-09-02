package me.fruits.fruits.utils;

import me.fruits.fruits.service.api.UserDTO;
import org.springframework.util.ObjectUtils;

public class ApiModuleRequestHolder {

    private final static ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void set(UserDTO userDTO) {
        threadLocal.set(userDTO);
    }

    public static UserDTO get() throws FruitsException {
        UserDTO userDTO = threadLocal.get();
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new FruitsException(ErrCode.TOKEN_ERR, "获取不到当前登录的用户");
        }
        return userDTO;
    }
}
