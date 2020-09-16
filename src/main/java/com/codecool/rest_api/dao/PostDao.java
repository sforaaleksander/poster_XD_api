package com.codecool.rest_api.dao;

import com.codecool.rest_api.models.Post;


public class PostDao extends AbstractDao<Post> {

    public PostDao() {
        super();
        this.aClass = Post.class;
    }
}
