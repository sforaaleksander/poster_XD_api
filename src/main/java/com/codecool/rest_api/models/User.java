package com.codecool.rest_api.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
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
    int id;
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
        return String.format("{" +
                "\"id\": \"%d\"" +
                ", \"name\": \"%s\"" +
                ", \"surname\": \"%s\"" +
                ", \"email\": \"%s\"" +
                "}", id, name, surname, email);
    }
}
