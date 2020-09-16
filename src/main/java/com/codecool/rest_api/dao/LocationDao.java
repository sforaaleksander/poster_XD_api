package com.codecool.rest_api.dao;

import com.codecool.rest_api.models.Location;

public class LocationDao extends AbstractDAO<Location> {

    public LocationDao() {
        super();
        this.aClass = Location.class;
    }
}
