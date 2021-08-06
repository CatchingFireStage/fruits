package me.fruits.fruits.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.service.admin.LoginService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/assist")
@RestController(value = "AdminAssistController")
@Api(tags = "打辅助")
public class AssistController {

    @Autowired
    private LoginService loginService;


    @ApiOperation("登录")
    @PostMapping("login")
    public Result<String> login() {

        String jwt = loginService.login("", "");

        return Result.success(jwt);
    }
}
