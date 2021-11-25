package me.fruits.fruits.service.user;

import me.fruits.fruits.mapper.enums.user.weChat.CarrierEnum;
import me.fruits.fruits.mapper.po.UserWeChat;
import me.fruits.fruits.service.user.dto.UserWeChatForAdminDTO;
import me.fruits.fruits.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserWeChatAdminModuleService {

    @Autowired
    private UserWeChatService userWeChatService;


    /**
     * 获取用户绑定的微信载体
     *
     * @param userId
     * @return
     */
    public List<UserWeChatForAdminDTO> getUserWeChatForAdminDTOList(long userId) {

        List<UserWeChat> userWeChats = userWeChatService.getUserWeChats(userId);

        List<UserWeChatForAdminDTO> response = new ArrayList<>();

        userWeChats.forEach(userWeChat -> {

            UserWeChatForAdminDTO userWeChatForAdminDTO = new UserWeChatForAdminDTO();

            userWeChatForAdminDTO.setId(userWeChat.getId());
            userWeChatForAdminDTO.setCarrier(EnumUtils.changeToString(CarrierEnum.class, userWeChat.getCarrier()));
            userWeChatForAdminDTO.setOpenId(userWeChat.getOpenId());


            response.add(userWeChatForAdminDTO);

        });


        return response;
    }
}
