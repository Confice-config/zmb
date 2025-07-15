package com.example.mapper;

import com.example.enetity.UserfileDTO;

public interface UserfileDTOMapper {
    UserfileDTO selectUserProfileWithDept(Long userId);

    void userfileDTOupdate(UserfileDTO userfileDTO);
}
