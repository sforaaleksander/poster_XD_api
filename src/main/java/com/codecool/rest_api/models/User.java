package com.codecool.rest_api.models;

import com.google.gson.JsonObject;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public @Data class User implements Indexable, Jsonable, Containable<Post> {
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String surname;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String email;
    @Column(name = "is_active", nullable = false)
    boolean isActive;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Comment> comments;

    protected User() {}

    public User(String name, String surname, String password, String email, boolean isActive) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
    }

    public String toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", this.id);
        obj.addProperty("name", this.name);
        obj.addProperty("surname", this.surname);
        obj.addProperty("email", this.email);
        obj.addProperty("posts", String.format("http://localhost:8080/users/%d/posts", id));
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
}
