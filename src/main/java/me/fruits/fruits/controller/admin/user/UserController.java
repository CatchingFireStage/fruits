package me.fruits.fruits.controller.admin.user;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fruits.fruits.controller.AdminLogin;
import me.fruits.fruits.mapper.po.User;
import me.fruits.fruits.service.user.UserAdminModuleService;
import me.fruits.fruits.utils.PageVo;
import me.fruits.fruits.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController(value = "AdminUserController")
@Api(tags = "用户")
@AdminLogin
@Validated
public class UserController {


    @Autowired
    private UserAdminModuleService userAdminModuleService;

    @GetMapping(value = "/user")
    @ApiOperation("列表页-用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "手机号", example = "")
    })
    public Result<User> user(
            @RequestParam(defaultValue = "") String keyword,
            PageVo pageVo
    ) {

        IPage<User> users = userAdminModuleService.getUsers(keyword, pageVo);

        return Result.success(users.getTotal(), users.getPages(), users.getRecords());
    }
}
