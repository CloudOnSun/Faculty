package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.Spectator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class SpectatorHibernateRepository implements ISpectatorRepository{

    public final SessionFactory sessionFactory;

    public SpectatorHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void add(Spectator elem) {
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
    public void delete(Spectator elem) {

    }

    @Override
    public void update(Spectator elem, Integer id) {

    }

    @Override
    public Spectator findById(Integer id) {
        return null;
    }

    @Override
    public List<Spectator> findAll() {
        return null;
    }
}
