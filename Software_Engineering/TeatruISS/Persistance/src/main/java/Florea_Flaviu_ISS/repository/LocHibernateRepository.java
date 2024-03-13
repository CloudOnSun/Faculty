package Florea_Flaviu_ISS.repository;

import Florea_Flaviu_ISS.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class LocHibernateRepository implements ILocRepository{

    public final SessionFactory sessionFactory;

    public LocHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void add(Loc elem) {
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
    public void delete(Loc elem) {
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
    public void update(Loc elem, Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createSQLQuery("update Loc set loc=?, loja=?, rand=?, rezervare=?, spectacol=? where id=?");
            query.setParameter(1, elem.getLoc());
            query.setParameter(2, elem.getLoja());
            query.setParameter(3, elem.getRand());
            query.setParameter(4, elem.getRezervare().getId());
            query.setParameter(5, elem.getSpectacol().getId());
            query.setParameter(6, id);
            query.executeUpdate();
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public Loc findById(Integer id) {
        return null;
    }

    @Override
    public List<Loc> findAll() {
        List<Loc> locuri = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Loc");
            List<Object[]> locs = query.list();
            for(Object[] a : locs){
                var id = (int) a[0];
                var loc = (int) a[1];
                var loja = (int) a[2];
                var rand = (int) a[3];
                Integer rezervare = (Integer) a[4];
                Rezervare rezervareObiect = null;
                Spectacol spectacol = null;
                if (rezervare != null) {
                    var queryRezervare = session
                            .createNativeQuery("select * from Rezervare where id=?");
                    queryRezervare.setParameter(1, rezervare);
                    List<Object[]> rezervs = queryRezervare.list();
                    for (Object[] r : rezervs) {
                        var code = (int) r[1];
                        var idSpectacol = (int) r[2];
                        var idSpectator = (int) r[3];

                        var querySpectator = session
                                .createNativeQuery("select * from Spectator where id=?");
                        querySpectator.setParameter(1, idSpectator);
                        List<Object[]> spectators = querySpectator.list();
                        Spectator spectator = null;
                        for (Object[] s : spectators) {
                            spectator = new Spectator(s[2].toString(), s[3].toString(), s[1].toString());
                            spectator.setId(idSpectator);
                        }

                        rezervareObiect = new Rezervare(code, spectator, spectacol);
                        rezervareObiect.setId(rezervare);
                    }
                }


                var locSala = new Loc(loja, rand, loc, spectacol);
                locSala.setId(id);
                locSala.setRezervare(rezervareObiect);
                locuri.add(locSala);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return locuri;
    }

    @Override
    public List<Loc> findBySpectacol(Spectacol spectacol) {
        List<Loc> locuri = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session
                    .createNativeQuery("select * from Loc where spectacol=?");
            query.setParameter(1, spectacol.getId());
            List<Object[]> locs = query.list();
            for(Object[] a : locs){
                var id = (int) a[0];
                var loc = (int) a[1];
                var loja = (int) a[2];
                var rand = (int) a[3];
                Integer rezervare = (Integer) a[4];
                Rezervare rezervareObiect = null;
                if (rezervare != null) {
                    var queryRezervare = session
                            .createNativeQuery("select * from Rezervare where id=?");
                    queryRezervare.setParameter(1, rezervare);
                    List<Object[]> rezervs = queryRezervare.list();
                    for (Object[] r : rezervs) {
                        var code = (int) r[1];
                        var idSpectator = (int) r[3];

                        var querySpectator = session
                                .createNativeQuery("select * from Spectator where id=?");
                        querySpectator.setParameter(1, idSpectator);
                        List<Object[]> spectators = querySpectator.list();
                        Spectator spectator = null;
                        for (Object[] s : spectators) {
                            spectator = new Spectator(s[2].toString(), s[3].toString(), s[1].toString());
                            spectator.setId(idSpectator);
                        }

                        rezervareObiect = new Rezervare(code, spectator, spectacol);
                        rezervareObiect.setId(rezervare);
                    }
                }


                var locSala = new Loc(loja, rand, loc, spectacol);
                locSala.setId(id);
                locSala.setRezervare(rezervareObiect);
                locuri.add(locSala);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return locuri;
    }
}
