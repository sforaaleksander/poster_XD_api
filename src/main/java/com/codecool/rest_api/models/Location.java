package com.codecool.rest_api.models;

import javax.persistence.*;

@Entity
public class Location implements Indexable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @OneToOne(mappedBy = "location")
    Post post;

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
}
