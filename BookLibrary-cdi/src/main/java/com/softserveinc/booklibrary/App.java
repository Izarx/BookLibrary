package com.softserveinc.booklibrary;

import com.softserveinc.booklibrary.configuration.HibernateConfiguration;
import com.softserveinc.booklibrary.entity.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {


    public static void main(String[] args) {
        SessionFactory sessionFactory =
                new AnnotationConfigApplicationContext(HibernateConfiguration.class)
                        .getBean("SessionFactory", SessionFactory.class);

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            Author author = new Author();
            author.setFirstName("Ihor");
            author.setLastName("Zakharko");
            session.save(author);
            tx.commit();
            System.out.println("Author saved with ID = " + author.getAuthorId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
