package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.UserDao;
import com.codecool.rest_api.models.Post;
import com.codecool.rest_api.models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@WebServlet(name = "users", urlPatterns = {"/users/*"}, loadOnStartup = 1)
public class UserServlet extends PosterAbstractServlet<User> {

    //TODO test this
    {
        this.dao = new UserDao();
        this.objectName = "user";
        this.rootPath = "/users/";
        this.subPathName = "posts";
    }

    @Override
    protected void getSubPathObjects(HttpServletResponse resp, User object) throws IOException {
        Set<Post> posts = object.getPosts();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Post post : posts) {
            sb.append(post.toJson()).append(",\n");
        }
        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().println(removeLast2Chars(sb.toString()) + "]");
    }

    private String removeLast2Chars(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        return s.substring(0, s.length() - 2);
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
        dao.insert(user);
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
}
