package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.LocationDao;
import com.codecool.poster_xd_api.models.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class LocationServlet extends PosterAbstractServlet<Location, String> {

    {
        this.dao = new LocationDao();
        this.objectName = "comment";
        this.rootPath = "/comments/";
        this.subPathName = "";
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
    protected void getSubPathObjects(HttpServletResponse resp, Location location) throws IOException {

    }

    @Override
    protected void updateObject(JsonObject requestAsJson, Location location) {

    }
}
