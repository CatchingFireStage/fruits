package me.fruits.fruits.controller.mini;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("/assist")
@RestController(value = "MiniAssistController")
@Api(tags = "打辅助")
public class AssistController {
    
    @ApiOperation("生成登录token")
    @GetMapping("make/token")
    public HashMap<String, String> makeToken(
            @ApiParam("成员id") @RequestParam Long userId
    ) {

        return new HashMap<String, String>();
    }
}
