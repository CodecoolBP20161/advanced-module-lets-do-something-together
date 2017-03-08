package com.codecool.model;


import com.codecool.security.Role;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "`user`")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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