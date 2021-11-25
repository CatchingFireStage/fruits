package me.fruits.fruits.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.fruits.fruits.mapper.UserWeChatMapper;
import me.fruits.fruits.mapper.enums.user.weChat.CarrierEnum;
import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.utils.FruitsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserWeChatService {

    @Autowired
    private UserWeChatMapper userWeChatMapper;


    @Autowired
    private UserService userService;

    /**
     * 创建用户，渠道来源于微信小程序
     */
    @Transactional
    public UserWeChat addUserByMiniProgram(String openId, String phone) {

        if (phone == null || phone.equals("")) {
            throw new FruitsRuntimeException("手机号必填");
        }

        //创建一个用户
        long userId = userService.add(phone);

        UserWeChat userWeChat = new UserWeChat();

        userWeChat.setUserId(userId);
        //小程序来的
        userWeChat.setCarrier(CarrierEnum.MINI_PROGRAM.getValue());
        userWeChat.setOpenId(openId);

        userWeChatMapper.insert(userWeChat);

        return userWeChat;
    }

    /**
     * 获取用户，渠道来源于微信小程序
     *
     * @param openId 微信的openId
     */
    public UserWeChat getUserWeChatByMiniProgram(String openId) {

        QueryWrapper<UserWeChat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("carrier", CarrierEnum.MINI_PROGRAM.getValue());
        queryWrapper.eq("open_id", openId);

        return userWeChatMapper.selectOne(queryWrapper);
    }

    /**
     * 获取用户，渠道来源于微信小程序
     *
     * @param userId 用户id
     */
    public UserWeChat getUserWeChatByMiniProgramFormUserId(long userId) {

        QueryWrapper<UserWeChat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("carrier", CarrierEnum.MINI_PROGRAM.getValue());
        queryWrapper.eq("user_id", userId);

        return userWeChatMapper.selectOne(queryWrapper);
    }


    /**
     * 获取用户绑定的微信载体
     */
    public List<UserWeChat> getUserWeChats(long userId) {

        QueryWrapper<UserWeChat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        return userWeChatMapper.selectList(queryWrapper);
    }

}
