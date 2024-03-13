package concurs.repository;

import concurs.domain.Admin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;


public class AdminHibernateRepository implements IAdminRepository {
    private static final Logger logger = LogManager.getLogger();

    public final SessionFactory sessionFactory;

    public AdminHibernateRepository(SessionFactory sessionFactory) {
        logger.info("Initializing AdminHibernateRepository");
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Admin getAccount(String email, String password) {
        logger.traceEntry("getting account by email {} and password {}", email, password);
        Admin admin = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Admin where email=? and password=?");
            query.setParameter(1, email);
            query.setParameter(2, password);
            List<Object[]> admins = query.list();
            for(Object[] a : admins){
                admin = new Admin(a[1].toString(), a[2].toString(), (int)a[3]);
                admin.setID((int)a[0]);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        logger.traceExit(admin);
        return admin;
    }

    @Override
    public void add(Admin elem) {

    }

    @Override
    public void delete(Admin elem) {

    }

    @Override
    public void update(Admin elem, Integer id) {

    }

    @Override
    public Admin findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Admin> findAll() {
        return null;
    }

    @Override
    public Collection<Admin> getAll() {
        return null;
    }
}
