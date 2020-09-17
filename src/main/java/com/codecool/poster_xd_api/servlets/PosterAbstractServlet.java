package com.codecool.poster_xd_api.servlets;

import com.codecool.poster_xd_api.dao.AbstractDao;
import com.codecool.poster_xd_api.models.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class PosterAbstractServlet<T, S> extends HttpServlet {
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
            getObjectsForRoot(req, resp);
            return;
        }
        Optional<T> optionalObject = getObjectFromRequestPath(req);
        if (uriPointsToSubPath(req, subPathName) && optionalObject.isPresent()) {
            getSubPathObjects(resp, optionalObject.get());
            return;
        }
        if (optionalObject.isPresent()) {
            getSingleObject(resp, optionalObject.get());
            return;
        }
        resp.getWriter().println("could not find " + objectName);
    }

    protected void getObjectsForRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("not implemented");
    }

    private void getSingleObject(HttpServletResponse resp, T object) throws IOException {
        resp.setStatus(200);
        resp.setContentType("application/json");
        Jsonable jsonable = (Jsonable) object;
        resp.getWriter().println(jsonable.toJson());
    }

    protected void getSubPathObjects(HttpServletResponse resp, T object) throws IOException {
        Containable<S> container = (Containable<S>) object;
        Set<S> objects = container.getSubObjects();
        String objectsAsJsonString = objects.stream().map(e->(Jsonable)e).map(Jsonable::toJson).collect(Collectors.joining(",\n"));
        writeObjectsToResponseFromCollection(resp, objectsAsJsonString);
    }

    protected void writeObjectsToResponseFromCollection(HttpServletResponse resp, String objectsAsJsonString) throws IOException {
        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().println("[" + objectsAsJsonString + "]");
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
            dao.insert(optionalObject.get());
            resp.setStatus(201);
            Indexable object = (Indexable) optionalObject.get();
            resp.setHeader("location", rootPath + object.getId());
            resp.setContentType("application/json");
            resp.getWriter().println(((Jsonable) object).toJson());
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


    protected List<T> populateObjectList(List<List<T>> lists) {
        final List<T> objectList;
        if (lists.size() > 1) {
            for (int i = 1; i < lists.size(); i++) {
                lists.get(0).retainAll(lists.get(i));
            }
        }
        if (lists.size() > 0) {
            objectList = lists.get(0);
        } else {
            objectList = dao.getAll();
        }
        return objectList;
    }

    protected void addObjectsMatchingParameter(HttpServletRequest req, List<List<T>> lists, String parameter) {
        String value = req.getParameter(parameter);
        if (value != null) {
            List<T> list1 = dao.getObjectsByField(parameter, value);
            lists.add(list1);
        }
    }
}
