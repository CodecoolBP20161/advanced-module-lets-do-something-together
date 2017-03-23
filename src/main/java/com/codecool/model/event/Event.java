package com.codecool.model.event;

import com.codecool.model.Interest;
import com.codecool.model.User;
import com.sun.istack.internal.Nullable;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String location;
    private Date date;
    private int participants;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "interest")
    private Interest interest;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "`user`")
    private User user;

    @Nullable
    private Coordinates coordinates;

    public Event() {
    }

}
