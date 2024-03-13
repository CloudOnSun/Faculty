package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class RezervareHibernateRepository implements IRezervareRepository{

    public final SessionFactory sessionFactory;

    public RezervareHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void add(Rezervare elem) {
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
    public void delete(Rezervare elem) {
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
    public void update(Rezervare elem, Integer id) {

    }

    @Override
    public Rezervare findById(Integer id) {
        Rezervare rezervare = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var queryRezervare = session
                    .createNativeQuery("select * from Rezervare where id=?");
            queryRezervare.setParameter(1, id);
            List<Object[]> rezervs = queryRezervare.list();
            for (Object[] r : rezervs) {
                var code = (int) r[1];
                var idSpectacol = (int) r[2];
                var idSpectator = (int) r[3];
                Spectacol spectacol = null;

                List<Spectacol> spectacole = new ArrayList<>();
                var query = session
                        .createNativeQuery("select * from Spectacol where id=?");
                query.setParameter(1, idSpectacol);
                List<Object[]> spects = query.list();
                for (Object[] a : spects) {
                    var gen = Genuri.valueOf((int) a[1]);
                    var regizor = a[2].toString();
                    var titlu = a[3].toString();

                    var queryDate = session
                            .createNativeQuery("select * from DateTime where id=?");
                    queryDate.setParameter(1, a[4]);
                    List<Object[]> dates = queryDate.list();
                    DateTime dateTime = null;
                    for (Object[] d : dates) {
                        dateTime = new DateTime((int) d[5], (int) d[4], (int) d[1], (int) d[2], (int) d[3]);
                        dateTime.setId((int) d[0]);
                    }

                    var queryManager = session
                            .createNativeQuery("select * from Manager where id=?");
                    queryManager.setParameter(1, a[5]);
                    List<Object[]> mans = queryManager.list();
                    Manager manager = null;
                    for (Object[] m : mans) {
                        manager = new Manager(m[1].toString(), m[2].toString());
                        manager.setId((int) m[0]);
                    }

                    var queryTeatru = session
                            .createNativeQuery("select * from Teatru where id=?");
                    queryTeatru.setParameter(1, a[6]);
                    List<Object[]> teats = queryTeatru.list();
                    Teatru teatru = null;
                    for (Object[] t : teats) {
                        teatru = new Teatru(t[1].toString());
                        teatru.setId((int) t[0]);
                    }

                    var spect = new Spectacol(titlu, dateTime, regizor, gen, manager);
                    spect.setTeatru(teatru);
                    spect.setId((int) a[0]);
                    spectacole.add(spect);
                }
                spectacol = spectacole.get(0);

                var querySpectator = session
                        .createNativeQuery("select * from Spectator where id=?");
                querySpectator.setParameter(1, idSpectator);
                List<Object[]> spectators = querySpectator.list();
                Spectator spectator = null;
                for (Object[] s : spectators) {
                    spectator = new Spectator(s[2].toString(), s[3].toString(), s[1].toString());
                    spectator.setId(idSpectator);
                }

                rezervare = new Rezervare(code, spectator, spectacol);
                rezervare.setId(id);
                session.getTransaction().commit();
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return rezervare;
    }

    @Override
    public List<Rezervare> findAll() {
        return null;
    }
}
