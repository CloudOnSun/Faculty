package concurs.repository;

import concurs.domain.TakesPart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class ParticipationDBRepository implements IParticipationRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ParticipationDBRepository(Properties properties) {
        logger.info("Initializing ParicipationDBRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(TakesPart elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("" +
                "insert into TakesPart (contestant, race) values (?, ?)")) {
            preStmt.setInt(1, elem.getContestant().getID());
            preStmt.setInt(2, elem.getRace().getID());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(TakesPart elem) {

    }

    @Override
    public void update(TakesPart elem, Integer id) {

    }

    @Override
    public TakesPart findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<TakesPart> findAll() {
        return null;
    }

    @Override
    public Collection<TakesPart> getAll() {
        return null;
    }
}
