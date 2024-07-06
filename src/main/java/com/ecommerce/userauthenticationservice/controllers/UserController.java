package com.ecommerce.userauthenticationservice.controllers;

import com.ecommerce.userauthenticationservice.UserService;
import com.ecommerce.userauthenticationservice.dtos.UserDto;
import com.ecommerce.userauthenticationservice.util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Conversion conversion;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long userId){

        UserDto userDto = conversion.convertUserToDto(userService.getUserById(userId));

        return userDto;
    }
}
