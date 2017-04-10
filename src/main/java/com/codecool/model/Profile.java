package com.codecool.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@ToString(exclude = "interestList")
public class Profile {

    @OneToOne
    @JoinColumn(name = "`user`", unique = true)
    @NotNull
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String firstName;

    private String lastName, language, gender,location;
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "profile_interest",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "interest_id"}))
    private List<Interest> interestList;

    public Profile() {
    }

    public Profile(User user) {
        this.user = user;
        this.firstName = user.getEmail();
    }
}