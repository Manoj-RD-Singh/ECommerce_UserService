package com.ecommerce.userauthenticationservice.controllers;

import com.ecommerce.userauthenticationservice.dtos.SignUpRequestDto;
import com.ecommerce.userauthenticationservice.dtos.UserDto;
import com.ecommerce.userauthenticationservice.models.User;
import com.ecommerce.userauthenticationservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user = authService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        UserDto userDto = convertUserToDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    private UserDto convertUserToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setUserRoleSet(user.getUserRoleSet());
        return userDto;
    }
}
