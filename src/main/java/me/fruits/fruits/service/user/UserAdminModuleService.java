package me.fruits.fruits.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.UserMapper;
import me.fruits.fruits.mapper.po.User;
import me.fruits.fruits.service.user.dto.UserForAdminDTO;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAdminModuleService {

    @Autowired
    private UserMapper userMapper;

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

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like("phone", keyword);
        }


        return userMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
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
