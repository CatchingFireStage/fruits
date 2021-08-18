package me.fruits.fruits.utils;

import me.fruits.fruits.service.admin.AdminDTO;
import org.springframework.util.ObjectUtils;

/**
 * admin模块的RequestHolder
 */
public class AdminModuleRequestHolder {
    private final static ThreadLocal<AdminDTO> threadLocal = new ThreadLocal<>();

    public static void set(AdminDTO adminDTO) {
        threadLocal.set(adminDTO);
    }

    public static AdminDTO get() throws FruitsException {
        AdminDTO adminDTO = threadLocal.get();
        if (ObjectUtils.isEmpty(adminDTO)) {
            throw new FruitsException(ErrCode.TOKEN_ERR, "获取不到当前登录的用户");
        }
        return adminDTO;
    }
}
