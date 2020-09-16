package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.UserDao;
import com.codecool.rest_api.models.Post;
import com.codecool.rest_api.models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "users", urlPatterns = {"/users/*"}, loadOnStartup = 1)
public class UserServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);

        if (uriPointsToUsers(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }

        Optional<User> user = getUserByRequestPath(req);

        if (uriPointsToUserPosts(req) && user.isPresent()) {
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

    private boolean uriPointsToUserPosts(HttpServletRequest req) {
        String[] pathParts = req.getPathInfo().replaceFirst("/", "").split("/");
        return pathParts.length == 2 && pathParts[1].equals("posts");
    }

    private boolean uriPointsToUsers(HttpServletRequest req) {
        return req.getPathInfo() == null;
    }

    private Optional<User> getUserByRequestPath(HttpServletRequest req) {
        Optional<User> user = Optional.empty();

        String[] pathParts = req.getPathInfo().replaceFirst("/", "").split("/");
        String userId = pathParts[0];

        try {
            if (userId != null) {
                user = userDao.getById(Long.parseLong(userId));
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        return user;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        if (uriPointsToUsers(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }

        Optional<User> user = getUserByRequestPath(req);

        if (user.isPresent()) {
            userDao.delete(user.get().getId());
            resp.setStatus(204);
            return;
        }
        resp.getWriter().println("user not found");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!uriPointsToUsers(req)) {
            resp.setStatus(404);
            resp.getWriter().println("Not implemented");
            return;
        }
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<Long> userId = createRequestedUser(requestAsJson);

        if (userId.isPresent()) {
            resp.setStatus(201);
            resp.setHeader("location", "/users/" + userId.get());
            resp.getWriter().println("resource created successfully");
            return;
        }
        resp.setStatus(422);
        resp.getWriter().println("couldn't create user");
    }

    private JsonObject requestToJsonObject(HttpServletRequest req) throws IOException {
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return JsonParser.parseReader(req.getReader()).getAsJsonObject();
    }

    private Optional<Long> createRequestedUser(JsonObject requestAsJson) {
        JsonElement name = requestAsJson.get("name");
        if (name == null) return Optional.empty();

        JsonElement surname = requestAsJson.get("surname");
        if (surname == null) return Optional.empty();

        JsonElement email = requestAsJson.get("email");
        if (email == null) return Optional.empty();

        JsonElement password = requestAsJson.get("password");
        if (password == null) return Optional.empty();

        User user = new User(name.getAsString(), surname.getAsString(), password.getAsString(), email.getAsString(), true);
        userDao.insert(user);
        return Optional.of(user.getId());
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        if (uriPointsToUsers(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<User> user = getUserByRequestPath(req);

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

        userDao.update(user);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getMethod();

        if (method.equals("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
