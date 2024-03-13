package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class SpectacolHibernateRepository implements ISpectacolRepository{

    public final SessionFactory sessionFactory;

    public SpectacolHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Spectacol> filtrareDupaGen(Genuri gen) {
        List<Spectacol> spectacole = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Spectacol where gen=?");
            query.setParameter(1, gen.ordinal());
            List<Object[]> spects = query.list();
            for(Object[] a : spects){
                var id = (int) a[0];
                var regizor = a[2].toString();
                var titlu = a[3].toString();

                var queryDate = session
                        .createNativeQuery("select * from DateTime where id=?");
                queryDate.setParameter(1, a[4]);
                List<Object[]> dates = queryDate.list();
                DateTime dateTime = null;
                for(Object[] d: dates) {
                    dateTime = new DateTime((int) d[5], (int) d[4], (int) d[1], (int) d[2], (int) d[3]);
                    dateTime.setId((int) d[0]);
                }

                var queryManager = session
                        .createNativeQuery("select * from Manager where id=?");
                queryManager.setParameter(1, a[5]);
                List<Object[]> mans = queryManager.list();
                Manager manager = null;
                for(Object[] m: mans) {
                    manager = new Manager(m[1].toString(), m[2].toString());
                    manager.setId((int) m[0]);
                }

                var queryTeatru = session
                        .createNativeQuery("select * from Teatru where id=?");
                queryTeatru.setParameter(1, a[6]);
                List<Object[]> teats = queryTeatru.list();
                Teatru teatru = null;
                for(Object[] t: teats) {
                    teatru = new Teatru(t[1].toString());
                    teatru.setId((int) t[0]);
                }

                var spectacol = new Spectacol(titlu, dateTime, regizor, gen, manager);
                spectacol.setTeatru(teatru);
                spectacol.setId((int)a[0]);
                spectacole.add(spectacol);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return spectacole;
    }

    @Override
    public void add(Spectacol elem) {
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
    public void delete(Spectacol elem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(elem);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Override
    public void update(Spectacol elem, Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createSQLQuery("update Spectacol set gen=?, regizor=?, titlu=?, dateTime=?, manager=?, teatru=? where id=?");
            query.setParameter(1, elem.getGen().ordinal());
            query.setParameter(2, elem.getRegizor());
            query.setParameter(3, elem.getTitlu());
            query.setParameter(4, elem.getDateTime().getId());
            query.setParameter(5, elem.getManager().getId());
            query.setParameter(6, elem.getTeatru().getId());
            query.setParameter(7, id);
            query.executeUpdate();
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public Spectacol findById(Integer idUnic) {
        List<Spectacol> spectacole = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Spectacol where id=?");
            query.setParameter(1, idUnic);
            List<Object[]> spects = query.list();
            for(Object[] a : spects){
                var id = (int) a[0];
                var gen = Genuri.valueOf((int) a[1]);
                var regizor = a[2].toString();
                var titlu = a[3].toString();

                var queryDate = session
                        .createNativeQuery("select * from DateTime where id=?");
                queryDate.setParameter(1, a[4]);
                List<Object[]> dates = queryDate.list();
                DateTime dateTime = null;
                for(Object[] d: dates) {
                    dateTime = new DateTime((int) d[5], (int) d[4], (int) d[1], (int) d[2], (int) d[3]);
                    dateTime.setId((int) d[0]);
                }

                var queryManager = session
                        .createNativeQuery("select * from Manager where id=?");
                queryManager.setParameter(1, a[5]);
                List<Object[]> mans = queryManager.list();
                Manager manager = null;
                for(Object[] m: mans) {
                    manager = new Manager(m[1].toString(), m[2].toString());
                    manager.setId((int) m[0]);
                }

                var queryTeatru = session
                        .createNativeQuery("select * from Teatru where id=?");
                queryTeatru.setParameter(1, a[6]);
                List<Object[]> teats = queryTeatru.list();
                Teatru teatru = null;
                for(Object[] t: teats) {
                    teatru = new Teatru(t[1].toString());
                    teatru.setId((int) t[0]);
                }

                var spectacol = new Spectacol(titlu, dateTime, regizor, gen, manager);
                spectacol.setTeatru(teatru);
                spectacol.setId((int)a[0]);
                spectacole.add(spectacol);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return spectacole.get(0);
    }

    @Override
    public List<Spectacol> findAll() {

        List<Spectacol> spectacole = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Spectacol");
            List<Object[]> spects = query.list();
            for(Object[] a : spects){
                var id = (int) a[0];
                var gen = Genuri.valueOf((int) a[1]);
                var regizor = a[2].toString();
                var titlu = a[3].toString();

                var queryDate = session
                        .createNativeQuery("select * from DateTime where id=?");
                queryDate.setParameter(1, a[4]);
                List<Object[]> dates = queryDate.list();
                DateTime dateTime = null;
                for(Object[] d: dates) {
                    dateTime = new DateTime((int) d[5], (int) d[4], (int) d[1], (int) d[2], (int) d[3]);
                    dateTime.setId((int) d[0]);
                }

                var queryManager = session
                        .createNativeQuery("select * from Manager where id=?");
                queryManager.setParameter(1, a[5]);
                List<Object[]> mans = queryManager.list();
                Manager manager = null;
                for(Object[] m: mans) {
                    manager = new Manager(m[1].toString(), m[2].toString());
                    manager.setId((int) m[0]);
                }

                var queryTeatru = session
                        .createNativeQuery("select * from Teatru where id=?");
                queryTeatru.setParameter(1, a[6]);
                List<Object[]> teats = queryTeatru.list();
                Teatru teatru = null;
                for(Object[] t: teats) {
                    teatru = new Teatru(t[1].toString());
                    teatru.setId((int) t[0]);
                }

                var spectacol = new Spectacol(titlu, dateTime, regizor, gen, manager);
                spectacol.setTeatru(teatru);
                spectacol.setId((int)a[0]);
                spectacole.add(spectacol);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return spectacole;
    }
}
