package com.codecool.model.event;

import com.codecool.model.Interest;
import com.codecool.model.User;
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
    private Coordinates coordinates;
    private Date date;
    private int participants;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "interest")
    private Interest interest;

    @ManyToOne
    @JoinColumn(name = "`user`")
    private User user;

    public Event() {
    }

}
