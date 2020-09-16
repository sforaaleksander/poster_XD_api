package com.codecool.rest_api.servlets;

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

    protected JsonObject requestToJsonObject(HttpServletRequest req) throws IOException {
        String jsonString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //TODO why method parseString() can not be resolve in static context
        return new JsonParser().parse(jsonString).getAsJsonObject();
    }

    abstract void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
