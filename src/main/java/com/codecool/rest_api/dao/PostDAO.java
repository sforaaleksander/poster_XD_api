package com.codecool.rest_api.dao;

import com.codecool.rest_api.models.Post;


public class PostDAO extends AbstractDAO<Post>{

    public PostDAO() {
        super();
        this.aClass = Post.class;
    }
}
