package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.LocationDAO;
import com.codecool.rest_api.dao.PostDAO;
import com.codecool.rest_api.dao.UserDao;
import com.codecool.rest_api.models.Location;
import com.codecool.rest_api.models.Post;
import com.codecool.rest_api.models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@WebServlet(name = "posts", urlPatterns = {"/posts/*"})

public class PostServlet extends PosterAbstractServlet<Post> {

    {
        this.dao = new PostDAO();
        this.objectName = "post";
        this.rootPath = "/posts/";
        this.subPathName = "comments";
    }

    private final PostDAO postDAO = new PostDAO();
    private final UserDao userDao = new UserDao();
    private final LocationDAO locationDAO = new LocationDAO();


    @Override
    protected Optional<Post> createPojoFromJsonObject(JsonObject jsonObject) {
        JsonElement locationIdJson = jsonObject.get("location");
        JsonElement dateJson = jsonObject.get("date");
        JsonElement contentJson = jsonObject.get("content");
        JsonElement userIdJson = jsonObject.get("user");

        if (locationIdJson == null || dateJson == null || contentJson == null ||
                userIdJson == null ) {
            return Optional.empty();
        }
        Optional<User> optionalUser = userDao.getById(userIdJson.getAsLong());
        Optional<Location> optionalLocation = locationDAO.getById(locationIdJson.getAsLong());
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dateJson.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        };
        if (!optionalUser.isPresent() || !optionalLocation.isPresent() || date == null) {
            return Optional.empty();
        }

        String content = contentJson.getAsString();
        return Optional.of(new Post(optionalUser.get(), optionalLocation.get(), date, content));
    }

    @Override
    protected void getSubPathObjects(HttpServletResponse resp, Post post) throws IOException {
        //TODO
    }

    @Override
    protected void updateObject(JsonObject requestAsJson, Post post) {
        //TODO
    }
}
