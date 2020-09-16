package com.codecool.rest_api;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public enum EntityManagerProvider {
    INSTANCE;
    private final EntityManager entityManager = Persistence
            .createEntityManagerFactory("posterPU")
            .createEntityManager();

    public EntityManager getEntityManager(){
        return entityManager;
    }
}
