package com.ecommerce.userauthenticationservice.util;

import com.ecommerce.userauthenticationservice.dtos.UserDto;
import com.ecommerce.userauthenticationservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class Conversion {

    public UserDto convertUserToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setUserRoleSet(user.getUserRoleSet());
        return userDto;
    }

}
