package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.Jsonable;
import com.codecool.poster_xd_api.models.Post;
import com.codecool.poster_xd_api.models.User;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "users", urlPatterns = {"/users/*"}, loadOnStartup = 1)
public class UserServlet extends PosterAbstractServlet<User, Post> {

    {
        this.dao = new UserDao();
        this.objectName = "user";
        this.rootPath = "/users/";
        this.subPathName = "posts";
    }

    @Override
    protected Optional<User> createPojoFromJsonObject(JsonObject requestAsJson) {
        if (!(requestAsJson.has("name")
                && requestAsJson.has("surname")
                && requestAsJson.has("email")
                && requestAsJson.has("password"))) {
            return Optional.empty();
        }
        User user = new User(
                requestAsJson.get("name").getAsString(),
                requestAsJson.get("surname").getAsString(),
                requestAsJson.get("email").getAsString(),
                requestAsJson.get("password").getAsString(),
                true);
        return Optional.of(user);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, User user) {
        if (jsonObject.has("name")) {
            user.setName(jsonObject.get("name").getAsString());
        }
        if (jsonObject.has("surname")) {
            user.setSurname(jsonObject.get("surname").getAsString());
        }
        if (jsonObject.has("email")) {
            user.setEmail(jsonObject.get("email").getAsString());
        }
        dao.update(user);
    }

    @Override
    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> userList;
        List<List<User>> lists = new ArrayList<>();

        addObjectsMatchingParameter(req, lists, "surname");
        addObjectsMatchingParameter(req, lists, "name");
        addObjectsMatchingParameter(req, lists, "isActive");

        userList = populateObjectList(lists);

        String objectsAsJsonString = userList
                .stream()
                .map(e -> (Jsonable) e)
                .map(Jsonable::toJson)
                .collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);
    }
}
