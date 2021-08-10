package me.fruits.fruits.service.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.utils.AdminModuleRequestHolder;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class LoginAdminModuleService {

    @Value("${fruits.admin.username}")
    private String username;

    @Value("${fruits.admin.password}")
    private String password;

    private static final String SECRET = "123fruits123";

    @Autowired
    private HttpServletRequest request;

    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @return jwt
     */
    public String login(String username, String password) {

        if (username.compareTo(this.username) != 0 || password.compareTo(this.password) != 0) {
            return null;
        }

        //过期时间,1天
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expires = nowTime.plusDays(1);

        JWTCreator.Builder builder = JWT.create()
                .withAudience("fruits-admin")
                .withClaim("adminId", 1)
                .withClaim("username", username)
                .withExpiresAt(Date.from(expires.atZone(ZoneId.systemDefault()).toInstant()));


        return builder.sign(Algorithm.HMAC256(SECRET));
    }


    /**
     * 注入jwtToken
     */
    public void injectJwtTokenContext() throws FruitsException {
        String accessTokenAdmin = request.getHeader("access-token-admin");
        if(accessTokenAdmin == null){
            throw new FruitsException(FruitsException.TOKEN_ERR, "token异常");
        }
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(accessTokenAdmin);
            Claim adminId = verify.getClaim("adminId");
            Claim username = verify.getClaim("username");
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(adminId.asLong());
            adminDTO.setName(username.asString());
            AdminModuleRequestHolder.set(adminDTO);
        } catch (JWTVerificationException e) {
            throw new FruitsException(FruitsException.TOKEN_ERR, "token异常");
        }
    }
}
