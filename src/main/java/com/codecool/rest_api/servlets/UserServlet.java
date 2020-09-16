package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.UserDao;
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
import java.util.stream.Collectors;

@WebServlet(name = "users", urlPatterns = {"/users/*"}, loadOnStartup = 1)
public class UserServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] pathParts = req.getPathInfo().replaceFirst("/", "").split("/");

        Optional<User> user = userDao.getById(Long.parseLong(pathParts[0]));

        resp.setContentType("application/json");
        resp.getWriter().println(user.isPresent()
                ? user.get().toJson()
                : "User not found");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<User> user = getRequestedUser(requestAsJson);

        if (user.isPresent()) {
            userDao.delete(user.get().getId());
            resp.setStatus(204);
            resp.getWriter().println("resource deleted successfully");
            return;
        }
        resp.setStatus(404);
        resp.getWriter().println("user not found");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<User> user = getRequestedUser(requestAsJson);

        if (user.isPresent()) {
            updateUser(requestAsJson, user.get());
            resp.setStatus(204);
            resp.getWriter().println("resource updated successfully");
            return;
        }
        resp.setStatus(404);
        resp.getWriter().println("user not found");
    }

    private JsonObject requestToJsonObject(HttpServletRequest req) throws IOException {
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return JsonParser.parseString(requestBody).getAsJsonObject();
    }

    private Optional<User> getRequestedUser(JsonObject reqAsJson) {
        Optional<User> user = Optional.empty();

        JsonElement id = reqAsJson.get("id");
        if (id != null) user = userDao.getById(Long.parseLong(id.getAsString()));
        return user;
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
