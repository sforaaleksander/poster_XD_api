package com.codecool.poster_xd_api.helpers;

import com.codecool.poster_xd_api.models.Comment;
import com.codecool.poster_xd_api.models.Location;
import com.codecool.poster_xd_api.models.Post;
import com.codecool.poster_xd_api.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DBSetter {
    public void init(){
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("posterPU");
    EntityManager em = emf.createEntityManager();

    populateDb(em);
        em.clear();

        em.close();
        emf.close();
}

    static void persistObjects(EntityManager em, Object... objects) {
        for (Object object : objects) {
            em.persist(object);
        }
    }

    public static void populateDb(EntityManager em) {
        DateParser dp = new DateParser();

        Location cracow = new Location("Cracow", 50.096898F, 19.936306F);
        Location warsaw = new Location("Warsaw", 52.229675F, 21.012230F);
        Location budapest = new Location("Budapest", 44.426765F, 26.102537F);

        User jan = new User("Jan", "Kowalski", "123", "j@gmail.com", true);
        User balazs = new User("Balázs", "Faragó", "123", "b@gmail.com", true);
        User mateusz = new User("Mateusz", "Golda", "123", "m@gmail.com", true);

        Post mPost1 = new Post(mateusz, cracow, dp.parseDate("2020-09-01"), "some content");
        Post mPost2 = new Post(mateusz, warsaw, dp.parseDate("2020-09-02"), "some content2");
        Comment mComment1 = new Comment(mPost1, dp.parseDate("2020-09-05"), jan, "Nice content bro!");
        Comment mComment2 = new Comment(mPost1, dp.parseDate("2020-09-06"), balazs, "Very impressive!");
        Comment mComment3 = new Comment(mPost1, dp.parseDate("2020-09-06"), mateusz, "Thanks guys");

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        persistObjects(em, mPost1, mPost2, cracow, warsaw, budapest, jan, balazs, mateusz, mComment1, mComment2, mComment3);
        transaction.commit();
        System.out.println("\n### Matthew saved.\n");

        User alek = new User("Alek", "Jednaszewski", "123", "a@gmail.com", true);
        Post aPost = new Post(alek, budapest, dp.parseDate("2020-09-07"), "test post");
        Comment comment2 = new Comment(aPost, dp.parseDate("2020-09-09"), mateusz, "test comment");

        transaction.begin();
        persistObjects(em, alek, aPost, comment2);
        transaction.commit();
        System.out.println("\n### Alek saved.\n");
    }
}
