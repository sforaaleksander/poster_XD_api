package com.codecool.rest_api.dao;

import com.codecool.rest_api.models.User;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super();
        this.aClass = User.class;
    }
}
