package com.codecool.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "userEmail")
@Data
public class UserEmail {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "emailSent")
    @NotNull
    private boolean emailSent = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`user`")
    @NotNull
    private User user;
}
