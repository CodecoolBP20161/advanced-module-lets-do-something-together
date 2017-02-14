package com.codecool.security;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roles")
public enum Role {
    USER, ADMIN;

    private int id;
    private String role;

    Role() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    @NotNull
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "role")
    @NotNull
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
