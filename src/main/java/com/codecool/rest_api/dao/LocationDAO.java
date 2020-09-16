package com.codecool.rest_api.dao;

import com.codecool.rest_api.models.Location;

public class LocationDAO extends AbstractDAO<Location> {

    public LocationDAO() {
        super();
        this.aClass = Location.class;
    }
}
