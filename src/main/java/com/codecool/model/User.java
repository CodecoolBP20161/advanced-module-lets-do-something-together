package com.codecool.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {


    private Integer id;
    private String email;
    private String password;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    @NotNull
    public Integer getId() {
        return id;
    }

    @NotNull
    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    @NotNull
    @Column(name = "password")
    public String getPassword() {
        return password;
    }
}
