package com.codecool.poster_xd_api.models;

import com.codecool.poster_xd_api.helpers.DateParser;
import com.google.gson.JsonObject;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post implements Indexable, Jsonable, Containable<Comment> {
    @ManyToOne(fetch = FetchType.LAZY)
    Location location;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    Date date;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Comment> comments;

    protected Post() {
    }

    public Post(User user, Location location, Date date, String content) {
        this.user = user;
        this.location = location;
        this.date = date;
        this.content = content;
    }

    public Post(User user, Location location, String content) {
        this.user = user;
        this.location = location;
        this.date = new Date();
        this.content = content;
    }

    public String toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("id", this.getId());
        object.addProperty("user", this.user.getId());
        object.addProperty("location", this.location.getId());
        object.addProperty("date", new DateParser().dateToString(date));
        object.addProperty("content", this.content);
        return object.toString();
    }

    @Override
    public Set<Comment> getSubObjects() {
        return comments;
    }

    @Override
    public long getId() {
        return this.id;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
