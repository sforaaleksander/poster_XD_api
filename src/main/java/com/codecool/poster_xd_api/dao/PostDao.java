package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.Post;
import com.codecool.poster_xd_api.models.User;

import java.util.List;


public class PostDao extends AbstractDao<Post> {

    public PostDao() {
        super();
        this.aClass = Post.class;
    }

    @Override
    public List<User> getObjectsByField(String fieldName, String fieldValue) {
        return null;
    }
}
