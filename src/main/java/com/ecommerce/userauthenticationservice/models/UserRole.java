package com.ecommerce.userauthenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class UserRole extends BaseModel{
    private String value;
    @ManyToMany(mappedBy = "userRoleSet")
    private Set<User> userSet;
}
