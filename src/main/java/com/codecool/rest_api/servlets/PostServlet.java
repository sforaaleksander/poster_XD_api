package com.codecool.rest_api.servlets;

import com.codecool.rest_api.DateParser;
import com.codecool.rest_api.dao.LocationDao;
import com.codecool.rest_api.dao.PostDAO;
import com.codecool.rest_api.dao.UserDao;
import com.codecool.rest_api.models.Comment;
import com.codecool.rest_api.models.Location;
import com.codecool.rest_api.models.Post;
import com.codecool.rest_api.models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;


@WebServlet(name = "posts", urlPatterns = {"/posts/*"})

public class PostServlet extends PosterAbstractServlet<Post> {
    UserDao userDao = new UserDao();
    LocationDao locationDao = new LocationDao();

    {
        this.dao = new PostDAO();
        this.objectName = "post";
        this.rootPath = "/posts/";
        this.subPathName = "comments";
    }

    @Override
    protected void getSubPathObjects(HttpServletResponse resp, Post object) throws IOException {
        Set<Comment> comments = object.getComments();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Comment comment : comments) {
            sb.append(comment.toJson()).append(",\n");
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
    protected Optional<Post> createPojoFromJsonObject(JsonObject requestAsJson) {
        JsonElement userId = requestAsJson.get("user");
        if (userId == null) return Optional.empty();

        JsonElement locationId = requestAsJson.get("location");
        if (locationId == null) return Optional.empty();

        JsonElement dateString = requestAsJson.get("date");
        if (dateString == null) return Optional.empty();

        JsonElement content = requestAsJson.get("content");
        if (content == null) return Optional.empty();

        User user = userDao.getById(userId.getAsLong()).get();
        Location location = locationDao.getById(locationId.getAsLong()).get();
        Date date1 = new DateParser().parseDate(dateString.getAsString());

        Post post = new Post(user, location, date1, content.getAsString());
        dao.insert(post);
        return Optional.of(post);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, Post post) {
        JsonElement locationId = jsonObject.get("location");
        if (locationId != null) {
            Optional<Location> optionalLocation = locationDao.getById(locationId.getAsLong());
            if (optionalLocation.isPresent()) {
                post.setLocation(optionalLocation.get());
            } else {
                return;
            }
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
