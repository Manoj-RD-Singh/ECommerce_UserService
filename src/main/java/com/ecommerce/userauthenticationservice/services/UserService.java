package com.ecommerce.userauthenticationservice.services;

import com.ecommerce.userauthenticationservice.Repositories.UserRepository;
import com.ecommerce.userauthenticationservice.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User getUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found with id : "+id);
        }
        return userOptional.get();
    }
}
