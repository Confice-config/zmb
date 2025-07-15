package com.example.controller;


import com.example.common.Result;
import com.example.enetity.UserfileDTO;
import com.example.service.UserfileDTOService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserfileDTOController {
    @Resource
    UserfileDTOService userfileDTOService;
    @GetMapping("/profile-with-dept")
    public Result getUserProfileWithDept(@RequestParam Long userId) {
        UserfileDTO profile = userfileDTOService.getUserProfileWithDept(userId);
        return Result.success(profile);
    }
    @PutMapping("/profile-with-update")
    public Result userfileDTOupdate(@RequestBody UserfileDTO userfileDTO){
        userfileDTOService.userfileDTOupdate(userfileDTO);
        return Result.success();
    }


}
