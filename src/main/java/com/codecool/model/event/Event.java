package com.codecool.model.event;

import com.codecool.model.Interest;
import com.codecool.model.User;
import com.sun.istack.internal.Nullable;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private Date date;
    @NotNull
    private int participants;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String description;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "interest")
    private Interest interest;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "`user`")
    private User user;

    private Coordinates coordinates;

    public Event() {
    }

}
