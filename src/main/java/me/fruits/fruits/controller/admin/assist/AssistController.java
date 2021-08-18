package me.fruits.fruits.controller.admin.assist;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.admin.assist.vo.LoginRequest;
import me.fruits.fruits.service.admin.LoginAdminModuleService;
import me.fruits.fruits.utils.ErrCode;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/assist")
@RestController(value = "AdminAssistController")
@Api(tags = "打辅助")
//对RequestParam中的数据验证，get方法,@Valid是requestBody里面的
@Validated
public class AssistController {

    @Autowired
    private LoginAdminModuleService loginService;


    @ApiOperation("登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody @Valid LoginRequest loginRequest) throws FruitsException {

        String jwt = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (jwt == null) {
            throw new FruitsException(ErrCode.DEFAULT_ERR, "账号或者密码不对");
        }

        return Result.success(jwt);
    }
}
