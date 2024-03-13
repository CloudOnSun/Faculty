package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.Manager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ManagerHibernateRepository implements IManagerRepository{

    public final SessionFactory sessionFactory;

    public ManagerHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Manager logIn(String email, String password) {
        Manager manager = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Manager where email=? and password=?");
            query.setParameter(1, email);
            query.setParameter(2, password);
            List<Object[]> managers = query.list();
            for(Object[] a : managers){
                manager = new Manager(a[1].toString(), a[2].toString());
                manager.setId((int)a[0]);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return manager;
    }

    @Override
    public void add(Manager elem) {

    }

    @Override
    public void delete(Manager elem) {

    }

    @Override
    public void update(Manager elem, Integer id) {

    }

    @Override
    public Manager findById(Integer id) {
        return null;
    }

    @Override
    public List<Manager> findAll() {
        return null;
    }
}
