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
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(member);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMember(Member member) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Member member1 = session.get(Member.class, member.getId());
            member1.setEmail(member.getEmail());
            member1.setId(member.getId());
            member1.setMobile(member.getMobile());
            member1.setName(member.getName());
            session.update(member1);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMember(Member member) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(member);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(mailInfo);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
