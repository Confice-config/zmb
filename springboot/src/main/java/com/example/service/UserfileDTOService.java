package com.example.service;


import com.example.enetity.UserfileDTO;
import com.example.mapper.UserfileDTOMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserfileDTOService {
    @Resource
    UserfileDTOMapper userfileDTOMapper;
    public UserfileDTO getUserProfileWithDept(Long userId) {
        return userfileDTOMapper.selectUserProfileWithDept(userId);
    }

    public void userfileDTOupdate(UserfileDTO userfileDTO) {
        userfileDTOMapper.userfileDTOupdate(userfileDTO);
    }
}
