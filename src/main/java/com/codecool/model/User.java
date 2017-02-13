package com.codecool.model;


import com.codecool.security.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "`user`")
public class User {

    private Integer id;
    private String email;
    private String password;
    private Collection<Role> roles;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Column(name = "roles", nullable = false)
    @Enumerated(EnumType.STRING)
    @ManyToMany(mappedBy = "userCollection", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public Collection<Role> getRole() {
        return roles;
    }

    public void setRole(Collection<Role> roles) {
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    @NotNull
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
