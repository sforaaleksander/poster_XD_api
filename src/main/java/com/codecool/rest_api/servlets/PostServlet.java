package com.codecool.rest_api.servlets;

import com.codecool.rest_api.models.Post;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name = "posts", urlPatterns = {"/posts/*"})
public class PostServlet extends HttpServlet {

//    private EntityManager entityManager;
//
//    public PostServlet() {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaexamplePU");
//        this.entityManager = emf.createEntityManager();
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String postId = req.getPathInfo().replace("/", "");
        List<String> elements = Arrays.stream(req.getRequestURI().split("/")).filter(e -> !e.equals("")).collect(Collectors.toList());
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaexamplePU");
//        EntityManager em = emf.createEntityManager();
//        Post post = em.find(Post.class, elements.get(elements.size()-1));
//        Post post = findPost(Integer.parseInt(elements.get(elements.size()-1)));
        PrintWriter out = resp.getWriter();
//        out.println(post.toString());
//        elements.forEach(out::println);

        out.println("not implemented");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    public Post findPost(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaexamplePU");
        EntityManager em = emf.createEntityManager();

        em.clear();

        Post post = em.find(Post.class, id);
        em.close();
        emf.close();
        return post;
    }
}
