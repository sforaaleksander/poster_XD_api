package com.codecool.rest_api.models;

import java.util.Set;

public interface Containable<T> {

    Set<T> getSubObjects();
}
