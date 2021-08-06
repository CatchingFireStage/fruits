package me.fruits.fruits.service.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {

    @Value("${fruits.admin.username}")
    private String username;

    @Value("${fruits.admin.password}")
    private String password;
    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @return jwt
     */
    public String login(String username, String password) {

        if (username.compareTo(this.username) != 0 || password.compareTo(this.password) != 0){
            return null;
        }

        JWTCreator.Builder builder = JWT.create()
                .withAudience(AUDIENCE)
                .withClaim("userId",userId)
                .withExpiresAt(exDate);

    }
}
