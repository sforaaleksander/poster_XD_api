package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.DateParser;
import com.codecool.poster_xd_api.EntityManagerProvider;
import com.codecool.poster_xd_api.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractDao<T> implements IDao<T> {
    protected final EntityManager entityManager;
    protected Class<T> aClass;

    protected AbstractDao() {
        this.entityManager = EntityManagerProvider.INSTANCE.getEntityManager();
    }

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(entityManager.find(aClass, id));
    }

    @Override
    public void insert(T object) {
        handleTransaction(entityManager::persist, object);
    }

    @Override
    public void update(T object) {
        insert(object);
    }

    @Override
    public void delete(Long id) {
        Optional<T> optional = getById(id);
        optional.ifPresent(t -> handleTransaction(entityManager::remove, t));
    }

    @Override
    public List<T> getAll() {
        TypedQuery<T> query = entityManager.createQuery("SELECT u FROM " + aClass.getSimpleName() + " u", aClass);
        return query
                .getResultList();
    }

    private void handleTransaction(Consumer<T> method, T object) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        method.accept(object);
        transaction.commit();
    }

    public List<T> getObjectsByField(String fieldName, String fieldValue) {
        try {
            return isFieldType(fieldName, boolean.class)
                    ? getObjectsByObjectField(fieldName, Boolean.valueOf(fieldValue.toLowerCase()))
                    : isFieldType(fieldName, Date.class)
                    ? getObjectsByObjectField(fieldName, new DateParser().parseDate(fieldValue.toLowerCase()))
                    : isFieldType(fieldName, User.class)
                    ? getObjectsByObjectField(fieldName, new UserDao().getById(Long.parseLong(fieldValue.toLowerCase())).get())
                    : getObjectsByStringField(fieldName, fieldValue);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    protected List<T> getObjectsByStringField(String fieldName, String fieldValue) {
        TypedQuery<T> query = entityManager.createQuery(
                "SELECT u FROM " + aClass.getSimpleName() + " u WHERE lower(u." + fieldName + ") LIKE :value", aClass);
        return query
                .setParameter("value", "%" + fieldValue.toLowerCase() + "%")
                .getResultList();
    }

    protected List<T> getObjectsByObjectField(String fieldName, Object o) {
        TypedQuery<T> query = entityManager.createQuery(
                "SELECT u FROM " + aClass.getSimpleName() + " u WHERE u." + fieldName + " = :value", aClass);
        return query
                .setParameter("value", o)
                .getResultList();
    }

    private boolean isFieldType(String fieldName, Class<?> toCompareClass) {
        try {
            Class<?> fieldClass = aClass.getDeclaredField(fieldName).getType();
            if (fieldClass == toCompareClass) {
                return true;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }
}
