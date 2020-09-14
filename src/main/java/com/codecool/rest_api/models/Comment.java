package com.codecool.rest_api.models;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @Column(nullable = false)
    private int postId;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String content;
}
