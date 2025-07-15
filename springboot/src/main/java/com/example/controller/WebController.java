package com.example.controller;


import com.example.common.Result;
import com.example.enetity.User;
import com.example.exception.CustomerEeception;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
       // 1. 校验用户名和密码是否为空
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new CustomerEeception("用户名或密码不能为空");
        }
        User dbuser = userService.login(user);
        return Result.success(dbuser);
    }

}
