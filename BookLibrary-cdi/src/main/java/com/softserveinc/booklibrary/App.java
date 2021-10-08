package com.softserveinc.booklibrary;

import com.softserveinc.booklibrary.configuration.HibernateConfiguration;
import com.softserveinc.booklibrary.entity.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        logger.info("Test App logging start!");

        SessionFactory sessionFactory =
                new AnnotationConfigApplicationContext(HibernateConfiguration.class)
                        .getBean("SessionFactory", SessionFactory.class);

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()){
            tx = session.beginTransaction();
            logger.info("Transaction starts");
            Author author = new Author();
            author.setFirstName("Ihor");
            author.setLastName("Zakharko");
            session.save(author);
            tx.commit();
            logger.info("Author saved with ID = {}", author.getAuthorId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
