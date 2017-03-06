package com.codecool.model;


import com.codecool.security.Role;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "`user`")
@Data
public class User {

    private Integer id;
    private String email;
    private String password;
    private Role role;
    private String regDate;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}