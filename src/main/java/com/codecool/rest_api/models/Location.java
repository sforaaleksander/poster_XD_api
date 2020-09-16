package com.codecool.rest_api.models;

import javax.persistence.*;
import java.util.Set;

@Entity
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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public String toJson() {
        //TODO
        return null;
    }

    @Override
    public Set<Post> getSubObjects() {
        return posts;
    }
}
