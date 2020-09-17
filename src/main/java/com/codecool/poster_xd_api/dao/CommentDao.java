package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.Comment;

public class CommentDao extends AbstractDao<Comment>{

    public CommentDao() {
        super();
        this.aClass = Comment.class;
    }
}
