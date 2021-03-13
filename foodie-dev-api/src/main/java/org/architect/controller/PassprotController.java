package org.architect.controller;

import com.architect.pojo.Users;
import com.architect.pojo.bo.UsersBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.architect.service.UserService;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户校验接口
 *
 * @author 多宝
 * @since 2021/3/13 15:34
 */
@Api(value = "用户接口", tags = {"用户接口"})
@RestController
@RequestMapping("passport")
public class PassprotController {

    @Resource
    private UserService userService;

    @ApiOperation("校验用户名是否存在")
    @GetMapping("usernameIsExist")
    public ReturnResult usernameIsExist(@RequestParam String username) {
        // 判断入参不能为空
        if (StringUtils.isEmpty(username)) {
            return ReturnResult.errorMsg("用户名不能为空");
        }
        // 查找注册的用户名是否存在
        boolean isexist = userService.queryUserNameIsExist(username);
        if (isexist) {
            return ReturnResult.errorMsg("用户名已经存在");
        }
        return ReturnResult.ok();
    }

    @PostMapping("/regist")
    @ApiOperation("用户注册")
    public ReturnResult save(@RequestBody UsersBO usersBO) {
        String username = usersBO.getUsername();
        String password = usersBO.getPassword();
        String confirmPassword = usersBO.getConfirmPassword();

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)
        ) {
            return ReturnResult.errorMsg("用户名或者密码为空");
        }
        boolean isexist = userService.queryUserNameIsExist(username);
        if (isexist) {
            return ReturnResult.errorMsg("用户名已经存在");
        }
        if (password.length() < 6) {
            return ReturnResult.errorMsg("密码长度不能小于6");
        }
        if (!password.equals(confirmPassword)) {
            return ReturnResult.errorMsg("两次密码输入不一致");
        }
        Users user = userService.createUser(usersBO);

        return ReturnResult.ok(user);
    }

}
