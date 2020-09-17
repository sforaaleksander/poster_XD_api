package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.DateParser;
import com.codecool.poster_xd_api.dao.LocationDao;
import com.codecool.poster_xd_api.dao.PostDao;
import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.*;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@WebServlet(name = "posts", urlPatterns = {"/posts/*"})
public class PostServlet extends PosterAbstractServlet<Post, Comment> {
    UserDao userDao = new UserDao();
    LocationDao locationDao = new LocationDao();

    {
        this.dao = new PostDao();
        this.objectName = "post";
        this.rootPath = "/posts/";
        this.subPathName = "comments";
    }

    @Override
    protected Optional<Post> createPojoFromJsonObject(JsonObject requestAsJson) {
        if (!(requestAsJson.has("user")
                && requestAsJson.has("location")
                && requestAsJson.has("content"))) {
            return Optional.empty();
        }

        Optional<User> optionalUser = userDao.getById(requestAsJson.get("user").getAsLong());
        Optional<Location> optionalLocation = locationDao.getById(requestAsJson.get("location").getAsLong());
        if (!(optionalUser.isPresent() && optionalLocation.isPresent())) {
            return Optional.empty();
        }
        Post post = new Post(optionalUser.get(), optionalLocation.get(), requestAsJson.get("content").getAsString());
        return Optional.of(post);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, Post post) {
        if (jsonObject.has("location")) {
            Optional<Location> optionalLocation = locationDao.getById(jsonObject.get("location").getAsLong());
            if (!optionalLocation.isPresent()) {
                return;
            }
            post.setLocation(optionalLocation.get());
        }
        if (jsonObject.has("content")) {
            post.setContent(jsonObject.get("content").getAsString());
        }
        dao.update(post);
    }

    @Override
    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Post> postList;
        List<List<Post>> lists = new ArrayList<>();
        addObjectsMatchingParameter(req, lists, "content");
        addObjectsMatchingParameter(req, lists, "date");
        postList = populateObjectList(lists);
        String objectsAsJsonString = postList.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);
    }
}
