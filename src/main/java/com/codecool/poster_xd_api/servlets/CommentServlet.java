package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.DateParser;
import com.codecool.poster_xd_api.dao.CommentDao;
import com.codecool.poster_xd_api.dao.PostDao;
import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class CommentServlet extends PosterAbstractServlet<Comment, String> {

    UserDao userDao = new UserDao();
    PostDao postDao = new PostDao();

    {
        this.dao = new CommentDao();
        this.objectName = "comment";
        this.rootPath = "/comments/";
    }

    @Override
    Optional<Comment> createPojoFromJsonObject(JsonObject jsonObject) {
        JsonElement idJson = jsonObject.get("id");
        if (idJson == null) return Optional.empty();

        JsonElement userIdJson = jsonObject.get("user");
        if (userIdJson == null) return Optional.empty();

        JsonElement dateString = jsonObject.get("date");
        if (dateString == null) return Optional.empty();

        JsonElement postIdJson = jsonObject.get("post");
        if (postIdJson == null) return Optional.empty();

        JsonElement content = jsonObject.get("content");
        if (content == null) return Optional.empty();

        Optional<User> optionalUser = userDao.getById(userIdJson.getAsLong());
        Optional<Post> optionalPost = postDao.getById(postIdJson.getAsLong());
        if (!(optionalUser.isPresent() && optionalPost.isPresent())) {
            return Optional.empty();
        }
        Date date1 = new DateParser().parseDate(dateString.getAsString());
        Comment comment = new Comment(optionalPost.get(), date1, optionalUser.get(), content.getAsString());
        return Optional.of(comment);
    }

    @Override
    protected void updateObject(JsonObject jsonObject, Comment comment) {
        JsonElement contentJson = jsonObject.get("content");
        if (contentJson != null) comment.setContent(contentJson.getAsString());
        dao.update(comment);
    }

    @Override
    protected void getSubPathObjects(HttpServletResponse resp, Comment object) throws IOException {
        resp.setStatus(400);
        resp.setContentType("application/json");
        resp.getWriter().println("wrong path provided");
    }
}

