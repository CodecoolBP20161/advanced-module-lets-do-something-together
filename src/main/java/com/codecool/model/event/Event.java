package com.codecool.model.event;

import com.codecool.model.Interest;
import com.codecool.model.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Coordinates location;
    private String date;
    private int participants;

    @ManyToOne
    @JoinColumn(name = "category")
    private Interest category;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "`user`")
    private User user;

    public Event(Coordinates location, String date, int participants, Interest category, String description, User user) {
        this.location = location;
        this.date = date;
        this.participants = participants;
        this.category = category;
        this.description = description;
        this.status = Status.ACTIVE;
        this.user = user;
    }
}
