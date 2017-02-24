package com.codecool.model;


import com.codecool.security.Role;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "`user`")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    @NotNull
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "userDetail")
    private UserDetail userDetail;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}