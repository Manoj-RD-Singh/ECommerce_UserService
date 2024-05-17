package com.ecommerce.userauthenticationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Entity
public class User extends BaseModel{
    private String email;
    private String password;
    @ManyToMany
    @JoinTable(name="user_role_mapping", joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<UserRole> userRoleSet = new HashSet<>();//object intialization so that it value never null

    @OneToMany(mappedBy = "user")
    private Set<Session> session;
}
