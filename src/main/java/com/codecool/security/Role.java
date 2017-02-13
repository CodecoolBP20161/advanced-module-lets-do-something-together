package com.codecool.security;

import com.codecool.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "roles")
public enum Role {
    USER, ADMIN;

    private int id;
    private String role;
    private Collection<User> userCollection;

    Role() {
        this.role = this.toString();
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_users",
            joinColumns = @JoinColumn(name = "roles", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userCollection", referencedColumnName = "id"))
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
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
