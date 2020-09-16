package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.LocationDAO;
import com.codecool.rest_api.dao.PostDAO;
import com.codecool.rest_api.dao.UserDao;
import com.codecool.rest_api.models.Location;
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
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;


@WebServlet(name = "posts", urlPatterns = {"/posts/*"})
public class PostServlet extends HttpServlet {

    private final PostDAO postDAO = new PostDAO();
    private final UserDao userDao = new UserDao();
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        Long postId = Long.valueOf(req.getPathInfo().replace("/", ""));
        Optional<Post> optionalPost = postDAO.getById(postId);
        if (optionalPost.isPresent()){
            out.println(optionalPost.get().toJSON());
        } else {
            out.println("could not find post of given id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        JsonObject jsonObject = requestToJsonObject(req);
        Optional<Post> post = createPojoFromJsonObject(jsonObject);
        postDAO.insert(post.get());
        out.println("inserted post: " + post.get().toJSON());
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        JsonObject jsonObject = requestToJsonObject(req);
        Optional<Post> post = createPojoFromJsonObject(jsonObject);
        postDAO.update(post.get());
        out.println("updated post: " + post.get().toJSON());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        Long postId = Long.parseLong(req.getPathInfo().replace("/", ""));
        System.out.println("long: "+postId);
        Optional<Post> optionalPost = postDAO.getById(postId);
        if (optionalPost.isPresent()) {
            postDAO.delete(postId);
            out.println("deleted post of id " + postId);
        } else {
            out.println("could not find post of id " + postId);
        }
    }

    private Optional<Post> createPojoFromJsonObject(JsonObject jsonObject) {
        JsonElement locationIdJson = jsonObject.get("location");
        JsonElement dateJson = jsonObject.get("date");
        JsonElement contentJson = jsonObject.get("content");
        JsonElement userIdJson = jsonObject.get("user");

        if (locationIdJson == null || dateJson == null || contentJson == null ||
                userIdJson == null ) {
            return Optional.empty();
        }
        User user = userDao.getById(userIdJson.getAsLong()).get();
        Location location = locationDAO.getById(locationIdJson.getAsLong()).get();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dateJson.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        };
        String content = contentJson.getAsString();

        return Optional.of(new Post(user, location, date, content));

    }

    private JsonObject requestToJsonObject(HttpServletRequest req) throws IOException {
        String jsonString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //TODO why method parseString() can not be resolve in static context
        return new JsonParser().parse(jsonString).getAsJsonObject();
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
