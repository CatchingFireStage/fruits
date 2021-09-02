package me.fruits.fruits.service.user;

import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.service.api.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserWeChatApiModuleService {

    @Autowired
    private UserWeChatService userWeChatService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;


    /**
     * 小程序登录,用户不存在的时候会创建它
     *
     * @return 返回 user_we_chat表id，用它去让用户获取手机号码，换取登录
     */
    @Transactional
    public long loginMiniProgram(String openId, String sessionKey) {

        //获取用户
        UserWeChat userWeChatByMiniProgram = userWeChatService.getUserWeChatByMiniProgram(openId);

        if (userWeChatByMiniProgram != null) {
            //已经存在

            //更新小程序的sessionKey
            userWeChatService.update(userWeChatByMiniProgram.getId(), sessionKey);


            return userWeChatByMiniProgram.getId();
        }

        //没有存在的用户，注册用户
        UserWeChat userWeChat = userWeChatService.addUserByMiniProgram(openId, sessionKey);

        return userWeChat.getId();
    }

    /**
     * 小程序登录，绑定手机号
     *
     * @return 登录的token
     */
    public String loginMiniProgram(long id, String phone) {

        //获取用户
        UserWeChat userWeChat = userWeChatService.getUserWeChatByMiniProgram(id);

        //更新用户的手机号
        userService.update(userWeChat.getUserId(), phone);

        return loginService.login(userWeChat.getUserId());
    }
}
