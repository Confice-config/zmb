package com.example.controller;


import com.example.common.Result;
import com.example.enetity.User;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {
    @Resource
    UserService userService;
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        System.out.println("接收到的 avatar: " + user.getAvatar());
        userService.add(user);
        return Result.success();
    }
    @PostMapping("/updatepassword")
    public Result updatepassword (@RequestBody User user) {
        userService.updatepass(user);
        return Result.success();
    }
}
