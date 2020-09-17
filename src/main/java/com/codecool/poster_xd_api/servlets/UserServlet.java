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
        List<User> userList = new ArrayList<>();
        List<List<User>> lists = new ArrayList<>();
        List<User> list1;
        List<User> list2;
        List<User> list3;

        String surname = req.getParameter("surname");
        if (surname != null) {
            list1 = dao.getObjectsByField("surname", surname);
            lists.add(list1);
        }

        String name = req.getParameter("name");
        if (name != null) {
            list2 = dao.getObjectsByField("name", name);
            lists.add(list2);
        }

        String isActive = req.getParameter("isActive");
        if (isActive != null) {
            list3 = dao.getObjectsByField("isActive", isActive);
            lists.add(list3);
        }

        if (!lists.isEmpty()) {
            userList = lists.get(0);
        }

//        Set<User> result = Sets.newHashSet(lists.get(0));
//        for (List<User> numbers : list) {
//            result = Sets.intersection(result, Sets.newHashSet(numbers));
//        }
//        for (List<User> list : lists) {
//
//        }

        String objectsAsJsonString = userList.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);

    }
}
