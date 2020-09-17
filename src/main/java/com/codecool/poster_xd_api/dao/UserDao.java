package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.User;

import javax.persistence.TypedQuery;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super();
        this.aClass = User.class;
    }

    @Override
    public List<User> getObjectsByField(String value, String fieldName) {
//        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.surname LIKE :value", User.class);
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.surname LIKE '%a%'", User.class);
//        query.setParameter("value", "%"+value+"%");
//        query.setParameter("value", "%a%");
        List<User> resultList = query.getResultList();
        return resultList;
    }
}
