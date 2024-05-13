package com.ecommerce.userauthenticationservice.dtos;

import com.ecommerce.userauthenticationservice.models.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;
    private Set<UserRole> userRoleSet = new HashSet<>();

}
