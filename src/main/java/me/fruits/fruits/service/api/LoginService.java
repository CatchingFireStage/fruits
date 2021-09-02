package me.fruits.fruits.service.api;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.utils.ErrCode;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service(value="apiLoginService")
@Slf4j
public class LoginService {

    private static final String SECRET = "456fruits654";


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


    private UserDTO decodeToken(String token) throws FruitsException {
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


    public void injectJwtTokenContext() {
    }

    /**
     * User模型表登录
     *
     * @return jwt
     */
    private String login(long userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        return encodeToken(userDTO);
    }
}
