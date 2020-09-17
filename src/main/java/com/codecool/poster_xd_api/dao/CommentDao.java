package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.Comment;
import com.codecool.poster_xd_api.models.User;

import java.util.List;

public class CommentDao extends AbstractDao<Comment>{

    public CommentDao() {
        super();
        this.aClass = Comment.class;
    }

    @Override
    public List<User> getObjectsByField(String fieldName, String fieldValue) {
        return null;
    }
}
