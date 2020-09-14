package com.codecool.rest_api;

import com.codecool.rest_api.models.Comment;
import com.codecool.rest_api.models.Location;
import com.codecool.rest_api.models.Post;
import com.codecool.rest_api.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaexamplePU");
        EntityManager em = emf.createEntityManager();

        populateDb(em);
        em.clear();

        em.close();
        emf.close();
    }

    public static void populateDb(EntityManager em) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = Calendar.getInstance().getTime();
        Date date2 = Calendar.getInstance().getTime();
        Date date3 = Calendar.getInstance().getTime();
        try {
            date1 = sdf.parse("2020-07-21");
            date2 = sdf.parse("2020-08-01");
            date3 = sdf.parse("2020-09-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User matthew = new User("Matthew", "Golda", "123", "m@gmail.com", true);
        Location location = new Location("Kraków", 50.096898F, 19.936306F);
        Post post = new Post(matthew, location, date1);
        Comment comment = new Comment(post, date2, matthew, "comment content");

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(post);
        em.persist(location);
        em.persist(matthew);
        em.persist(comment);
        transaction.commit();
        System.out.println("\n### Matthew saved.\n");

        User alek = new User("Alek", "Jednaszewski", "123", "a@gmail.com", true);
        Post post2 = new Post(alek, location, date2);
        Comment comment2 = new Comment(post2, date3, matthew, "comment content2");

        transaction.begin();
        em.persist(alek);
        em.persist(post2);
        em.persist(comment2);
        transaction.commit();
        System.out.println("\n### Alek saved.\n");
    }
}
