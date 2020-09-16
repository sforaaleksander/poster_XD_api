package com.codecool.rest_api.models;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Indexable{
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public Set<Post> getPosts() {
        return posts;
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
}
