package com.example.service;

import com.example.enetity.User;
import com.example.exception.CustomerEeception;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public  User selectById(String userId) {
        return userMapper.selectById(userId);
    }


    public User login(User user) {
//        验证账号是否存在
         User dbuser = userMapper.selectByUsername(user.getUsername());
         if(dbuser == null) {
             throw new CustomerEeception("账号不存在");
         }
//         验证密码是否正确
         if (!dbuser.getPassword().equals(user.getPassword())) {
            throw new CustomerEeception("账号或密码错误");
        }
//         创建token并返回给前端
         String token = TokenUtils.createToken(dbuser.getId()+"-"+ dbuser.getRoleId(),dbuser.getPassword());
         dbuser.setToken(token);
         return dbuser;
    }

    public void add(User user) {
        userMapper.insert(user);
    }

    public void updatepass(User user) {
        if (!user.getNewpassword().equals(user.getNew2password())) {
            throw new CustomerEeception("500","您两次输入的密码不一致");
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (!currentUser.getPassword().equals(user.getPassword())) {
            throw new CustomerEeception("500","原密码输入错误");
        }
        User user1 =userMapper.selectById(currentUser.getId().toString());
        user1.setPassword(user.getNewpassword());
        userMapper.updatepass(user1);
    }
}
