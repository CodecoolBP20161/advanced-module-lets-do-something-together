package com.codecool.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class UserEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    private boolean emailSent = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`user`")
    @NotNull
    private User user;
}
