package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.Post;

public class PostDao extends AbstractDao<Post> {

    public PostDao() {
        super();
        this.aClass = Post.class;
    }
}
