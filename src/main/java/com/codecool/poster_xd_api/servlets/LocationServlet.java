package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.LocationDao;
import com.codecool.poster_xd_api.models.Jsonable;
import com.codecool.poster_xd_api.models.Location;
import com.codecool.poster_xd_api.models.Post;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (!(jsonObject.has("name") || jsonObject.has("latitude") || jsonObject.has("longitude"))) {
            return Optional.empty();
        }
        String name = jsonObject.get("name").getAsString();
        float latitude = jsonObject.get("latitude").getAsFloat();
        float longitude = jsonObject.get("longitude").getAsFloat();
        return Optional.of(new Location(name, latitude, longitude));
    }

    @Override
    protected void updateObject(JsonObject jsonObject, Location location) {
        if (!jsonObject.has("name")) location.setName(jsonObject.get("name").getAsString());
        dao.update(location);
    }

    @Override
    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Location> locationList;
        List<List<Location>> lists = new ArrayList<>();
        addObjectsMatchingParameter(req, lists, "name");
        locationList = populateObjectList(lists);
        String objectsAsJsonString = locationList.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);
    }
}
