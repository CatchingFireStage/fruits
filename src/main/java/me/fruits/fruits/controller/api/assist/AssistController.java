package me.fruits.fruits.controller.api.assist;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.fruits.fruits.service.api.LoginService;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/assist")
@RestController(value = "ApiAssistController")
@Api(tags = "打辅助")
public class AssistController {


    @Autowired
    private LoginService loginService;

    @ApiOperation("生成登录token")
    @GetMapping("make/token")
    public Result<String> makeToken(
            @ApiParam("成员id") @RequestParam Long userId
    ) {

        return Result.success(loginService.login(userId));
    }
}
