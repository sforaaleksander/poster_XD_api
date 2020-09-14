package com.codecool.rest_api;

import com.codecool.rest_api.models.Comment;
import com.codecool.rest_api.models.Location;
import com.codecool.rest_api.models.Post;
import com.codecool.rest_api.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
        var dp = new DateParser();

        Location cracow = new Location("Cracow", 50.096898F, 19.936306F);
        Location warsaw = new Location("Warsaw", 52.229675F, 21.012230F);
        Location budapest = new Location("Budapest", 44.426765F, 26.102537F);
        
        User jan = new User("Jan", "Kowalski", "123", "j@gmail.com", true);
        User balazs = new User("Balázs", "Faragó", "123", "b@gmail.com", true);
        User matthew = new User("Matthew", "Golda", "123", "m@gmail.com", true);

        Post mPost1 = new Post(matthew, cracow, dp.parseDate("2020-09-01"), "some content");
        Post mPost2 = new Post(matthew, warsaw, dp.parseDate("2020-09-02"), "some content2");
        Comment mComment1 = new Comment(mPost1, dp.parseDate("2020-09-05"), jan, "nice content");
        Comment mComment2 = new Comment(mPost1, dp.parseDate("2020-09-05"), balazs, "test");
        Comment mComment3 = new Comment(mPost1, dp.parseDate("2020-09-05"), matthew, "comment content");

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(mPost1);
        em.persist(mPost2);
        em.persist(cracow);
        em.persist(warsaw);
        em.persist(budapest);
        em.persist(jan);
        em.persist(balazs);
        em.persist(matthew);
        em.persist(mComment1);
        em.persist(mComment2);
        em.persist(mComment3);
        transaction.commit();
        System.out.println("\n### Matthew saved.\n");

        User alek = new User("Alek", "Jednaszewski", "123", "a@gmail.com", true);
        Post aPost = new Post(alek, budapest, dp.parseDate("2020-09-07"), "test post");
        Comment comment2 = new Comment(aPost, dp.parseDate("2020-09-09"), matthew, "test comment");

        transaction.begin();
        em.persist(alek);
        em.persist(aPost);
        em.persist(comment2);
        transaction.commit();
        System.out.println("\n### Alek saved.\n");
    }
}
