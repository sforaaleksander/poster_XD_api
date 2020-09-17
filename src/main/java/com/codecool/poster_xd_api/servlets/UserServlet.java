package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.Jsonable;
import com.codecool.poster_xd_api.models.Post;
import com.codecool.poster_xd_api.models.User;
import com.google.gson.JsonElement;
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
        JsonElement name = requestAsJson.get("name");
        if (name == null) return Optional.empty();

        JsonElement surname = requestAsJson.get("surname");
        if (surname == null) return Optional.empty();

        JsonElement email = requestAsJson.get("email");
        if (email == null) return Optional.empty();

        JsonElement password = requestAsJson.get("password");
        if (password == null) return Optional.empty();

        User user = new User(name.getAsString(), surname.getAsString(), password.getAsString(), email.getAsString(), true);
        return Optional.of(user);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, User user) {
        JsonElement name = jsonObject.get("name");
        if (name != null) user.setName(name.getAsString());

        JsonElement surname = jsonObject.get("surname");
        if (surname != null) user.setSurname(surname.getAsString());

        JsonElement email = jsonObject.get("email");
        if (email != null) user.setEmail(email.getAsString());

        dao.update(user);
    }

    @Override
    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> userList;
        List<List<User>> lists = new ArrayList<>();

        addUsersMatchingParameter(req, lists, "surname");
        addUsersMatchingParameter(req, lists, "name");
        addUsersMatchingParameter(req, lists, "isActive");

        if (lists.size() > 1) {
            for (int i = 1; i < lists.size(); i++) {
                lists.get(0).retainAll(lists.get(i));
            }
        }
        if (lists.size() > 0) {
            userList = lists.get(0);
        } else {
            userList = dao.getAll();
        }

        String objectsAsJsonString = userList.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);

    }

    private void addUsersMatchingParameter(HttpServletRequest req, List<List<User>> lists, String parameter) {
        String surname = req.getParameter(parameter);
        if (surname != null) {
            List<User> list1 = dao.getObjectsByField(parameter, surname);
            lists.add(list1);
        }
    }
}
