package com.codecool.rest_api.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    Location location;

    @Column(nullable = false)
    Date date;
}
