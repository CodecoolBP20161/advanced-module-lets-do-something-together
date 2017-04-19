package com.codecool.email.model;

import com.codecool.model.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class WelcomeEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    private boolean emailSent = false;

    @OneToOne
    @JoinColumn(name = "`user`")
    @NotNull
    private User user;

    public WelcomeEmail(User user) {
        this.user = user;
    }

    public WelcomeEmail() {
    }
}
