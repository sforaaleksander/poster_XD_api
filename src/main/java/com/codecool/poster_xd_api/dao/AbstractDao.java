package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractDao<T> implements IDAO<T>{
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
        return null;
    }

    private void handleTransaction(Consumer<T> method, T object) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        method.accept(object);
        transaction.commit();
    }
}
