package me.fruits.fruits.service.user;

import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.service.api.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWeChatApiModuleService {

    @Autowired
    private UserWeChatService userWeChatService;

    @Autowired
    private LoginService loginService;




    /**
     * 小程序登录,用户不存在的时候会创建它
     *
     * @return 返回 user_we_chat表id，如果用户不存在，返回0，引导用户去获取手机号，然后注册
     */
    public long loginMiniProgram(String openId, String sessionKey) {


        //获取用户
        UserWeChat userWeChatByMiniProgram = userWeChatService.getUserWeChatByMiniProgram(openId);

        if (userWeChatByMiniProgram != null) {
            //已经存在

            //更新小程序的sessionKey
            userWeChatService.update(userWeChatByMiniProgram.getId(), sessionKey);


            return userWeChatByMiniProgram.getId();
        }

        return 0;
    }

    /**
     * 注册并登录用户
     * @return 登录的token
     */
    public String registerUserByMiniProgram(String openId, String phone,String sessionKey) {

        /**
         * 注册用户
         */
        UserWeChat userWeChat = userWeChatService.addUserByMiniProgram(openId, phone, sessionKey);


        return loginService.login(userWeChat.getUserId());
    }
}
