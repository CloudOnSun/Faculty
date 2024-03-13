package concurs.repository;

import concurs.domain.Contestant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ContestantDBRepository implements IContestantRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ContestantDBRepository(Properties properties) {
        logger.info("Initializing ContestantDBRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Contestant> getByRace(String distance, String style) {
        logger.traceEntry("finding by race with distance {} and style {}", distance, style);
        Connection con = dbUtils.getConnection();
        List<Contestant> contestants = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select Con.ID as id, Con.name as name, Con.age as age " +
                        "from Contestant as Con inner join TakesPart as Part " +
                        "on Con.ID=Part.contestant " +
                        "inner join SwimmingRace as Race " +
                        "on Part.race=Race.ID " +
                        "where Race.distance=? and Race.style=?")) {
            preStmt.setString(1, distance);
            preStmt.setString(2, style);
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    Contestant cont = new Contestant(name, age);
                    cont.setID(id);
                    contestants.add(cont);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(contestants);
        return contestants;
    }

    @Override
    public void add(Contestant elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("" +
                "insert into Contestant (ID, name, age) values (?, ?, ?)")) {
            preStmt.setInt(1, elem.getID());
            preStmt.setString(2, elem.getName());
            preStmt.setInt(3, elem.getAge());
            int result = preStmt.executeUpdate();
            if (result>0){
                //obtinem ID-ul generat de baza de date
                ResultSet rs = preStmt.getGeneratedKeys();
                if (rs.next()) {
                    int id=rs.getInt(1);
                    elem.setID(id);
                    logger.trace("Generated id {} ",id);
                }

            }
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Contestant elem) {

    }

    @Override
    public void update(Contestant elem, Integer id) {

    }

    @Override
    public Contestant findById(Integer id) {
        logger.traceEntry("finding by id {}", id);
        Connection con = dbUtils.getConnection();
        Contestant contestant = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Contestant where ID=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    contestant = new Contestant(name, age);
                    contestant.setID(id);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(contestant);
        return contestant;
    }

    @Override
    public Iterable<Contestant> findAll() {
        return null;
    }

    @Override
    public Collection<Contestant> getAll() {
        return null;
    }
}
