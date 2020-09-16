package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.LocationDao;
import com.codecool.poster_xd_api.models.Location;
import com.codecool.poster_xd_api.models.Post;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import java.util.Optional;

@WebServlet(name = "locations", urlPatterns = {"/locations/*"}, loadOnStartup = 1)
public class LocationServlet extends PosterAbstractServlet<Location, Post> {

    {
        this.dao = new LocationDao();
        this.objectName = "location";
        this.rootPath = "/locations/";
        this.subPathName = "posts";
    }

    @Override
    Optional<Location> createPojoFromJsonObject(JsonObject jsonObject) {
        JsonElement nameJson = jsonObject.get("name");
        JsonElement latitudeJson = jsonObject.get("latitude");
        JsonElement longitudeJson = jsonObject.get("longitude");
        if (nameJson == null || latitudeJson == null || longitudeJson == null) {
            return Optional.empty();
        }
        String name = nameJson.getAsString();
        float latitude = latitudeJson.getAsFloat();
        float longitude = longitudeJson.getAsFloat();

        return Optional.of(new Location(name, latitude, longitude));
    }

    @Override
    protected void updateObject(JsonObject requestAsJson, Location location) {
        JsonElement name = requestAsJson.get("name");
        if (name != null) location.setName(name.getAsString());

        dao.update(location);
    }
}
