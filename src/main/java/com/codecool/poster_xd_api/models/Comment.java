package com.codecool.poster_xd_api.models;

import com.codecool.poster_xd_api.DateParser;
import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comments")
public class Comment implements Indexable, Jsonable {

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

    @Override
    public String toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("post", this.post.getId());
        object.addProperty("user", this.user.getId());
        object.addProperty("date", new DateParser().dateToString(date));
        object.addProperty("content", this.content);
        return object.toString();
    }

    public void setContent(String asString) {
        this.content = content;
    }
}
