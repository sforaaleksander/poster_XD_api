package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.CommentDao;
import com.codecool.poster_xd_api.dao.PostDao;
import com.codecool.poster_xd_api.dao.UserDao;
import com.codecool.poster_xd_api.models.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "comments", urlPatterns = {"/comments/*"}, loadOnStartup = 1)
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
        if (!(jsonObject.has("user")
                && jsonObject.has("post")
                && jsonObject.has("content"))) {
            return Optional.empty();
        }

        Optional<User> optionalUser = userDao.getById(jsonObject.get("user").getAsLong());
        Optional<Post> optionalPost = postDao.getById(jsonObject.get("post").getAsLong());

        if (!(optionalUser.isPresent() && optionalPost.isPresent())) {
            return Optional.empty();
        }
        Comment comment = new Comment(optionalPost.get(), optionalUser.get(), jsonObject.get("content").getAsString());
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
        resp.setStatus(404);
        resp.getWriter().println("Wrong path provided");
    }

    @Override
    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Comment> commentList;
        List<List<Comment>> lists = new ArrayList<>();

        addObjectsMatchingParameter(req, lists, "date");
        addObjectsMatchingParameter(req, lists, "user");
        addObjectsMatchingParameter(req, lists, "content");

        commentList = populateObjectList(lists);

        String objectsAsJsonString = commentList
                .stream()
                .map(e -> (Jsonable) e).map(Jsonable::toJson)
                .collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);
    }
}
