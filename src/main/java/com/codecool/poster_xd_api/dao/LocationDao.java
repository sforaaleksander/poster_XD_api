package com.codecool.poster_xd_api.dao;

import com.codecool.poster_xd_api.models.Location;
import com.codecool.poster_xd_api.models.User;

import java.util.List;

public class LocationDao extends AbstractDao<Location> {

    public LocationDao() {
        super();
        this.aClass = Location.class;
    }
}
