package me.fruits.fruits.service.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class LoginService {

    @Value("${fruits.admin.username}")
    private String username;

    @Value("${fruits.admin.password}")
    private String password;

    private static final String SECRET = "123fruits123";

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
}
