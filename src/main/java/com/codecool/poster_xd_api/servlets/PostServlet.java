package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.DateParser;
import com.codecool.poster_xd_api.dao.LocationDao;
import com.codecool.poster_xd_api.dao.PostDao;
import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.Comment;
import com.codecool.poster_xd_api.models.Location;
import com.codecool.poster_xd_api.models.Post;
import com.codecool.poster_xd_api.models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import java.util.Date;
import java.util.Optional;


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
        JsonElement userId = requestAsJson.get("user");
        if (userId == null) return Optional.empty();

        JsonElement locationId = requestAsJson.get("location");
        if (locationId == null) return Optional.empty();

        JsonElement dateString = requestAsJson.get("date");
        if (dateString == null) return Optional.empty();

        JsonElement content = requestAsJson.get("content");
        if (content == null) return Optional.empty();


        Optional<User> optionalUser = userDao.getById(userId.getAsLong());
        Optional<Location> optionalLocation = locationDao.getById(locationId.getAsLong());

        //TODO check if Java 11 works fine to refine this expression as optional.isEmpty()
        if (!(optionalUser.isPresent() && optionalLocation.isPresent())) {
            return Optional.empty();
        }
        Date date1 = new DateParser().parseDate(dateString.getAsString());
        Post post = new Post(optionalUser.get(), optionalLocation.get(), date1, content.getAsString());
        return Optional.of(post);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, Post post) {
        JsonElement locationId = jsonObject.get("location");
        if (locationId != null) {
            Optional<Location> optionalLocation = locationDao.getById(locationId.getAsLong());
            if (!optionalLocation.isPresent()) {
                return;
            }
            post.setLocation(optionalLocation.get());
        }

        JsonElement date = jsonObject.get("date");
        if (date != null) {
            post.setDate(new DateParser().parseDate(date.getAsString()));
        }

        JsonElement content = jsonObject.get("content");
        if (content != null) post.setContent(content.getAsString());

        dao.update(post);
    }
}
