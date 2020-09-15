package com.codecool.rest_api.dao;

import java.util.List;
import java.util.Optional;

public interface IDAO<T> {

    Optional<T> getById(Long id);

    void insert(T t);

    void update(T t);

    void delete(Long id);

    List<T> getAll();
}
