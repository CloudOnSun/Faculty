package concurs.repository;

import concurs.domain.Admin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class AdminDBRepository implements IAdminRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public AdminDBRepository(Properties properties) {
        logger.info("Initializing AdminDBRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Admin getAccount(String email, String password) {
        logger.traceEntry("getting account by email {} and password {}", email, password);
        Connection con = dbUtils.getConnection();
        Admin admin = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Admin where email=? and password=?")) {
            preStmt.setString(1, email);
            preStmt.setString(2, password);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    int officeID = result.getInt("officeID");
                    admin = new Admin(email, password, officeID);
                    admin.setID(id);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
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
