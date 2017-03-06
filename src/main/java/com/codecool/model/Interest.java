package com.codecool.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@ToString(exclude = "userDetails")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    @NotNull
    private int id;

    @Column
    private String activity;

    @ManyToMany(mappedBy = "interestList")
    private List<UserDetail> userDetails;

    public Interest() {
    }

    public Interest(String activity) {
        this.activity = activity;
    }
}
