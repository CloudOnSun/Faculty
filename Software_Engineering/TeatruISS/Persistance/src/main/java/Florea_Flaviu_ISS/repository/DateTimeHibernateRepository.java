package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.DateTime;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class DateTimeHibernateRepository implements IDateTimeRepository{

    public final SessionFactory sessionFactory;

    public DateTimeHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(DateTime elem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(elem);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Override
    public void delete(DateTime elem) {

    }

    @Override
    public void update(DateTime elem, Integer id) {

    }

    @Override
    public DateTime findById(Integer id) {
        return null;
    }

    @Override
    public List<DateTime> findAll() {
        return null;
    }
}
