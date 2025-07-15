package com.example.mapper;

import com.example.enetity.Employee;
import com.example.enetity.User;
import org.apache.ibatis.annotations.Select;


public interface UserMapper {

    @Select("select * from `user` where username= #{username}")
    User selectByUsername(String username);

    @Select("select id, username, password, role_id, created_at, employee_id from user where id = #{userId}")
    User selectById(String userId);

    void updateById(User user);

    void insert(User user);

    void updatepass(User user);
}
