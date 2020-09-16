package com.codecool.rest_api.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comments")
public class Comment implements Indexable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Post post;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String content;

    protected Comment() {}

    public Comment(Post post, Date date, User user, String content) {
        this.post = post;
        this.date = date;
        this.user = user;
        this.content = content;
    }

    @Override
    public long getId() {
        return id;
    }
}
