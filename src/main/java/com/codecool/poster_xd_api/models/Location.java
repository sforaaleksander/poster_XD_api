package com.codecool.poster_xd_api.models;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "locations")
public class Location implements Indexable, Jsonable, Containable<Post>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @OneToMany(mappedBy = "location", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    Set<Post> posts;

    protected Location() {}

    public Location(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("name", name);
        obj.addProperty("latitude", latitude);
        obj.addProperty("longitude", longitude);
        obj.addProperty("posts", String.format("http://localhost:8080/locations/%d/posts", id));
        return obj.toString();
    }

    @Override
    public Set<Post> getSubObjects() {
        return posts;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
