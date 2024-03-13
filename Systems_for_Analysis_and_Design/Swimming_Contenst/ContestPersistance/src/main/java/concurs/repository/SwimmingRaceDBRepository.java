package concurs.repository;

import concurs.domain.RaceNrContestantsDTO;
import concurs.domain.SwimmingRace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class SwimmingRaceDBRepository implements ISwimmingRaceRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public SwimmingRaceDBRepository(Properties props) {
        logger.info("Initializing SwimmingRaceDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<RaceNrContestantsDTO> nrContestantEachRace() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<RaceNrContestantsDTO> status = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select COUNT(Con.ID) as Nr, Race.ID as id, Race.distance as distance, Race.Style as style " +
                        "from SwimmingRace as Race left outer join TakesPart as Part " +
                        "on Race.ID=Part.race " +
                        "left outer join Contestant as Con " +
                        "on Part.contestant=Con.ID " +
                        "group by Race.ID, Race.distance, Race.Style")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int nr = result.getInt("Nr");
                    int id = result.getInt("id");
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    status.add(new RaceNrContestantsDTO(distance, style, nr));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(status);
        return status;
    }

    @Override
    public List<SwimmingRace> getByContestant(int idCont) {
        logger.traceEntry("finding which race does contestant {} takes part in", idCont);
        Connection con = dbUtils.getConnection();
        List<SwimmingRace> races = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select Race.ID as id, Race.distance as distance, Race.style as style " +
                        "from Contestant as Con inner join TakesPart as Part " +
                        "on Con.ID=Part.contestant " +
                        "inner join SwimmingRace as Race " +
                        "on Part.race=Race.ID " +
                        "where Con.ID=?")) {
            preStmt.setInt(1, idCont);
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id2 = result.getInt("id");
                    String distance2 = result.getString("distance");
                    String style2 = result.getString("style");
                    SwimmingRace race = new SwimmingRace(distance2, style2);
                    race.setID(id2);
                    races.add(race);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(races);
        return races;
    }

    @Override
    public SwimmingRace findByDistanceAndStyle(String distance, String style) {
        logger.traceEntry("finding by distance {} and style {}", distance, style);
        Connection con = dbUtils.getConnection();
        SwimmingRace race = null;
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from SwimmingRace where distance=? and style=?")) {
            preStmt.setString(1, distance);
            preStmt.setString(2, style);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    race = new SwimmingRace(distance, style);
                    race.setID(id);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(race);
        return race;
    }

    @Override
    public SwimmingRace[] getRaces() {

        logger.traceEntry("finding all");
        Connection con = dbUtils.getConnection();
        List<SwimmingRace> races = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from SwimmingRace")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    var race = new SwimmingRace(distance, style);
                    race.setID(id);
                    races.add(race);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        SwimmingRace[] races2 = races.toArray(new SwimmingRace[0]);
        logger.traceExit(races2);
        return races2;
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleteing race with id={}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("" +
                "delete from SwimmingRace where ID=?")) {
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void add(SwimmingRace elem) {
        logger.traceEntry("saving race {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("" +
                "insert into SwimmingRace (distance, style) values (?, ?)")) {
            preStmt.setString(1, elem.getDistance());
            preStmt.setString(2, elem.getStyle());
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
    public void delete(SwimmingRace elem) {

    }

    @Override
    public void update(SwimmingRace elem, Integer id) {
        logger.traceEntry("updating race with id {} with race", id, elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("" +
                "update SwimmingRace SET distance=?, style=? where ID=?")) {
            preStmt.setString(1, elem.getDistance());
            preStmt.setString(2, elem.getStyle());
            preStmt.setInt(3, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public SwimmingRace findById(Integer id) {

        logger.traceEntry("finding by id {}",id);
        Connection con = dbUtils.getConnection();
        SwimmingRace race = null;
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from SwimmingRace where ID=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    race = new SwimmingRace(distance, style);
                    race.setID(id);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(race);
        return race;
    }

    @Override
    public Iterable<SwimmingRace> findAll() {
        return null;
    }

    @Override
    public Collection<SwimmingRace> getAll() {
        return null;
    }
}
