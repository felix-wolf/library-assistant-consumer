package database;

import models.MailInfo;
import models.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseHandler {

    private SessionFactory sessionFactory;
    private static DatabaseHandler databaseHandler = null;

    public static DatabaseHandler getInstance() {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler();
        }
        return databaseHandler;
    }

    private DatabaseHandler() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public void insertMember(Member member) {
        int i = 0;
    }

    public void deleteMember() {
        int i = 0;
    }

    public void deleteAllMailServerInfo() {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.createQuery("delete from MailInfo").executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertMailServerInfo(MailInfo mailInfo) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(mailInfo);
        session.getTransaction().commit();
        session.close();
    }

}
