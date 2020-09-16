package com.codecool.rest_api.servlets;

import com.codecool.rest_api.dao.AbstractDao;
import com.codecool.rest_api.models.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class PosterAbstractServlet<T, V> extends HttpServlet {
    protected AbstractDao<T> dao;
    protected String objectName;
    protected String rootPath;
    protected String subPathName;

    abstract Optional<T> createPojoFromJsonObject(JsonObject jsonObject);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if (method.equals("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);

        if (uriPointsToRoot(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }
        Optional<T> optionalObject = getObjectFromRequestPath(req);
        if (uriPointsToSubPath(req, subPathName) && optionalObject.isPresent()) {
            getSubPathObjects(resp, optionalObject.get());
            return;
        }
        if (optionalObject.isPresent()) {
            resp.setStatus(200);
            resp.setContentType("application/json");
            Jsonable object = (Jsonable) optionalObject.get();
            resp.getWriter().println(object.toJson());
            return;
        }
        resp.getWriter().println("could not find " + objectName);
    }

    protected void getSubPathObjects(HttpServletResponse resp, T object) throws IOException {
        Containable<V> container = (Containable<V>) object;
        Set<V> objects = container.getSubObjects();
        String postsJsonString = objects.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().println("[" + postsJsonString + "]");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!uriPointsToRoot(req)) {
            resp.setStatus(404);
            resp.getWriter().println("not implemented");
            return;
        }
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<T> optionalObject = createPojoFromJsonObject(requestAsJson);

        if (optionalObject.isPresent()) {
            resp.setStatus(201);
            Indexable object = (Indexable) optionalObject.get();
            resp.setHeader("location", rootPath + object.getId());
            resp.getWriter().println("resource created successfully");
            return;
        }
        resp.setStatus(422);
        resp.getWriter().println("could not create " + objectName);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        if (uriPointsToRoot(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }
        JsonObject requestAsJson = requestToJsonObject(req);
        Optional<T> optionalObject = getObjectFromRequestPath(req);
        if (optionalObject.isPresent()) {
            updateObject(requestAsJson, optionalObject.get());
            resp.setStatus(204);
            return;
        }
        resp.getWriter().println("user not found");
    }

    protected abstract void updateObject(JsonObject requestAsJson, T t);

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        if (uriPointsToRoot(req)) {
            resp.getWriter().println("Not implemented");
            return;
        }
        Optional<T> optionalObject = getObjectFromRequestPath(req);
        if (optionalObject.isPresent()) {
            Indexable object = (Indexable) optionalObject.get();
            dao.delete(object.getId());
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
}
