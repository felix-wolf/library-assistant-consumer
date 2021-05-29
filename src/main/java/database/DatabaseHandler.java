package database;

import models.MailInfo;
import models.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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

    public Member getMember(Member member) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            member = session.get(Member.class, member.getId());
            session.getTransaction().commit();
            session.close();
            return member;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMember(Member member) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            member = session.get(Member.class, member.getId());
            member.setEmail(member.getEmail());
            member.setId(member.getId());
            member.setMobile(member.getMobile());
            member.setName(member.getName());
            session.update(member);
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

    public MailInfo getMailServerInfo() {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MailInfo> criteria = builder.createQuery(MailInfo.class);
            criteria.from(MailInfo.class);
            List<MailInfo> data = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            session.close();
            return data.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

}
