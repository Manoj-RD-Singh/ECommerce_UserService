package com.ecommerce.userauthenticationservice.springSecurity.services;

import com.ecommerce.userauthenticationservice.Repositories.UserRepository;
import com.ecommerce.userauthenticationservice.models.User;
import com.ecommerce.userauthenticationservice.springSecurity.models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("User not found : Signup first");
        }

        return new CustomUserDetails(userOptional.get());
    }
}
