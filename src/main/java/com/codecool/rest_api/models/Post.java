package com.codecool.rest_api.models;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @OneToOne
    Location location;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    protected Post() {}

    public Post(User user, Location location, Date date) {
        this.user = user;
        this.location = location;
        this.date = date;
    }
}
