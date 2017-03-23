package com.codecool.model;


import com.codecool.security.Role;
import com.sun.istack.internal.Nullable;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "`user`")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String email;
    private String password;
    @Nullable
    private UUID token;

    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String regDate;

    public User() {
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}