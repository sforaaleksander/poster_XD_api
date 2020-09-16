package com.codecool.poster_xd_api.models;

import com.google.gson.JsonObject;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
public @Data class Location implements Indexable, Jsonable, Containable<Post>{

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
        obj.addProperty("id", this.id);
        obj.addProperty("name", this.name);
        obj.addProperty("latitude", this.latitude);
        obj.addProperty("longitude", this.longitude);
        obj.addProperty("posts", String.format("http://localhost:8080/locations/%d/posts", id));
        return obj.toString();
    }

    @Override
    public Set<Post> getSubObjects() {
        return posts;
    }
}
