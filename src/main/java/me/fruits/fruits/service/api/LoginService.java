package me.fruits.fruits.service.api;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.User;
import me.fruits.fruits.utils.ApiModuleRequestHolder;
import me.fruits.fruits.utils.ErrCode;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service(value="apiLoginService")
@Slf4j
public class LoginService {


    public static final  String HEADER_TOKEN = "access-token-api";

    private static final String SECRET = "456fruits654";

    @Autowired
    private HttpServletRequest request;


    private String encodeToken(UserDTO userDTO) {


        //过期时间,1天
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expires = nowTime.plusDays(30);

        JWTCreator.Builder builder = JWT.create()
                .withAudience("fruits-api")
                .withClaim("id", userDTO.getId())
                .withExpiresAt(Date.from(expires.atZone(ZoneId.systemDefault()).toInstant()));


        return builder.sign(Algorithm.HMAC256(SECRET));
    }


    private UserDTO verifyToken(String token) throws FruitsException {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            Claim id = verify.getClaim("id");
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id.asLong());
            return userDTO;
        } catch (JWTVerificationException e) {
            throw new FruitsException(ErrCode.TOKEN_ERR, "token异常");
        }
    }



    public void injectJwtTokenContext() throws FruitsException {
        String accessTokenApi = request.getHeader(HEADER_TOKEN);

        if(accessTokenApi == null){
            throw new FruitsException(ErrCode.TOKEN_ERR, "token异常");
        }
        //token验证
        UserDTO userDTO = verifyToken(accessTokenApi);

        //context注入
        ApiModuleRequestHolder.set(userDTO);
    }

    /**
     * User模型表登录
     *
     * @return jwt
     */
    public String login(long userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        return encodeToken(userDTO);
    }
}
