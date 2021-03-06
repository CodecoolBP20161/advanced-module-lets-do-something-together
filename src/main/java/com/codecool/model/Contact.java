package com.codecool.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String message;
    private Date date;
    private boolean forwarded = false;

}
