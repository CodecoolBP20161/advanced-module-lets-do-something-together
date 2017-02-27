package com.codecool.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "user_detail")
@Data
@ToString(exclude = "interestList")
public class UserDetail {

    @OneToOne()
    @JoinColumn(name = "user_id")
    private  User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String firstName;

    private String lastName, language, gender;
    @Column(columnDefinition="TEXT")
    private String introduction;
    @ManyToMany(mappedBy = "userDetails")
    private List<Interest> interestList;

    public UserDetail() {
    }

    public UserDetail(User user) {
        this.user = user;
        this.firstName = user.getEmail();
    }
}
