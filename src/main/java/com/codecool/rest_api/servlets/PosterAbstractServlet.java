package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.AbstractDAO;
import com.codecool.rest_api.models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class PosterAbstractServlet<T> extends HttpServlet {
    protected AbstractDAO<T> dao;


    abstract Optional<T> createPojoFromJsonObject(JsonObject jsonObject);

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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        if (uriPointsToRoot(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }

        Optional<T> object = getObjectFromRequestPath(req);

        if (object.isPresent()) {
            dao.delete(object.get().getId());
            resp.setStatus(204);
            return;
        }
        resp.getWriter().println("user not found");
    }

    protected Optional<T> getObjectFromRequestPath(HttpServletRequest req) {
        Optional<T> optionalObject = Optional.empty();
        String objectId = getObjectIdFromPathInfo(req);
        try {
            if (objectId != null) {
                optionalObject = dao.getById(Long.parseLong(objectId));
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        return optionalObject;
    }

    private String getObjectIdFromPathInfo(HttpServletRequest req) {
        return req.getPathInfo().replaceFirst("/", "").split("/")[0];
    }

    protected JsonObject requestToJsonObject(HttpServletRequest req) throws IOException {
        return JsonParser.parseReader(req.getReader()).getAsJsonObject();
    }

    protected boolean uriPointsToRoot(HttpServletRequest req) {
        return req.getPathInfo() == null || req.getPathInfo().equals("/");
    }

    protected boolean uriPointsToSubPath(HttpServletRequest req, String path) {
        String[] pathParts = req.getPathInfo().replaceFirst("/", "").split("/");
        return pathParts.length == 2 && pathParts[1].equals(path);
    }

    abstract void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
