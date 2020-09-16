package com.codecool.poster_xd_api.models;

import java.util.Set;

public interface Containable<T> {

    Set<T> getSubObjects();
}
