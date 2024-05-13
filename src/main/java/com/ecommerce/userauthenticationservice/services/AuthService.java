package com.ecommerce.userauthenticationservice.services;

import com.ecommerce.userauthenticationservice.Repositories.UserRepository;
import com.ecommerce.userauthenticationservice.exceptions.UserAlreadyExistsDuringSignUpException;
import com.ecommerce.userauthenticationservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signUp(String email, String password){
        //Check user is present
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsDuringSignUpException("User already present");
        }
        //create new user and save in db
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }
}
