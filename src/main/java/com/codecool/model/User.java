package com.codecool.model;


import com.codecool.security.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "`user`")
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
//        Date today = Calendar.getInstance().getTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-kk:mm:ss");
//        this.regDate = formatter.format(today);
    }


    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Column(name = "regDate")
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
