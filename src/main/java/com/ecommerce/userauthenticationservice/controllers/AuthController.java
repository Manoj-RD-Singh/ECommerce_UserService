package com.ecommerce.userauthenticationservice.controllers;

import com.ecommerce.userauthenticationservice.dtos.LoginRequestDto;
import com.ecommerce.userauthenticationservice.dtos.SignUpRequestDto;
import com.ecommerce.userauthenticationservice.dtos.UserDto;
import com.ecommerce.userauthenticationservice.dtos.ValidateTokenRequestDto;
import com.ecommerce.userauthenticationservice.models.User;
import com.ecommerce.userauthenticationservice.services.AuthService;
import com.ecommerce.userauthenticationservice.util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private Conversion conversion;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user = authService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        UserDto userDto = conversion.convertUserToDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/loginByPassword")
    public ResponseEntity<UserDto> loginByPassword(@RequestBody LoginRequestDto loginRequestDto){
        User user = authService.loginByPassword(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        UserDto userDto = conversion.convertUserToDto(user);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("email", user.getEmail());
        headers.add("password", user.getPassword());

        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    @PostMapping("/loginByPasswordAndJWT")
    public ResponseEntity<UserDto> loginByPasswordAndJWT(@RequestBody LoginRequestDto loginRequestDto){
        Pair<User, MultiValueMap<String, String>> pair = authService.loginByPasswordAndJWT(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        UserDto userDto = conversion.convertUserToDto(pair.getFirst());
        return new ResponseEntity<>(userDto, pair.getSecond(), HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
        Boolean validate = authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
        if (validate) {
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Token is not valid", HttpStatus.BAD_REQUEST);
    }

}
