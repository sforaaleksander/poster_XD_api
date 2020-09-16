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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);

        if (uriPointsToRoot(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }

        Optional<User> user = getObjectFromRequestPath(req);

        if (uriPointsToSubPath(req, "posts") && user.isPresent()) {
            Set<Post> posts = user.get().getPosts();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Post post : posts) {
                sb.append(post.toJson()).append(",\n");
            }
            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.getWriter().println(removeLast2Chars(sb.toString()) + "]");
            return;
        }

        if (user.isPresent()) {
            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.getWriter().println(user.get().toJson());
            return;
        }
        resp.getWriter().println("User not found");
    }

    private String removeLast2Chars(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        return s.substring(0, s.length() - 2);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!uriPointsToRoot(req)) {
            resp.setStatus(404);
            resp.getWriter().println("Not implemented");
            return;
        }
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<User> optionalUser = createPojoFromJsonObject(requestAsJson);

        if (optionalUser.isPresent()) {
            resp.setStatus(201);
            resp.setHeader("location", "/users/" + optionalUser.get().getId());
            resp.getWriter().println("resource created successfully");
            return;
        }
        resp.setStatus(422);
        resp.getWriter().println("couldn't create user");
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
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        if (uriPointsToRoot(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<User> user = getObjectFromRequestPath(req);

        if (user.isPresent()) {
            updateUser(requestAsJson, user.get());
            resp.setStatus(204);
            return;
        }
        resp.getWriter().println("user not found");
    }

    private void updateUser(JsonObject jsonObject, User user) {
        JsonElement name = jsonObject.get("name");
        if (name != null) user.setName(name.getAsString());

        JsonElement surname = jsonObject.get("surname");
        if (surname != null) user.setSurname(surname.getAsString());

        JsonElement email = jsonObject.get("email");
        if (email != null) user.setEmail(email.getAsString());

        dao.update(user);
    }
}
