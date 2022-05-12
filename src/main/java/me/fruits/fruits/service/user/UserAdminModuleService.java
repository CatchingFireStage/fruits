package me.fruits.fruits.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.po.User;
import me.fruits.fruits.service.user.dto.UserForAdminDTO;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAdminModuleService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserWeChatAdminModuleService userWeChatAdminModuleService;

    /**
     * 列表页
     *
     * @param keyword 手机号
     * @param pageVo  分页
     * @return
     */
    public IPage<User> getUsers(String keyword, PageVo pageVo) {

        LambdaQueryChainWrapper<User> queryWrapper = userService.lambdaQuery();


        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like(User::getPhone, keyword);
        }

        return userService.page(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }


    /**
     * 获取UserForAdminDTO
     *
     * @param id 用户id
     */
    public UserForAdminDTO getUserForAdminDTO(long id) {

        //获取用户
        User user = userService.getUser(id);

        //响应
        UserForAdminDTO userForAdminDTO = new UserForAdminDTO();

        userForAdminDTO.setId(user.getId());
        userForAdminDTO.setPhone(user.getPhone());


        //获取用户绑定微信的实体信息
        userForAdminDTO.setWeChatBind(userWeChatAdminModuleService.getUserWeChatForAdminDTOList(user.getId()));

        return userForAdminDTO;
    }
}
