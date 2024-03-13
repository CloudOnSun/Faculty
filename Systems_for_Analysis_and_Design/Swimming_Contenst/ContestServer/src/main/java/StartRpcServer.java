import concurs.repository.*;
//import repository.*;
import server.ContestServiceImpl;
import service.IService;
import utils.AbstractServer;
import utils.ContestRpcConcurrentServer;
import utils.ServerException;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class StartRpcServer {

    private static int defaultPort = 55555;
    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            var metadataSources = new MetadataSources( registry );
            var builtMetadata = metadataSources.buildMetadata();
            sessionFactory = builtMetadata.buildSessionFactory();
        }
        catch (Exception e) {
            //e.getCause();
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        initialize();
//        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//            public void run() {
//                close();
//            }
//        }, "Shutdown-thread"));
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties + " + e);
            return;
        }
        //IAdminRepository adminRepo = new AdminDBRepository(serverProps);
        IAdminRepository adminRepo = new AdminHibernateRepository(sessionFactory);
        IContestantRepository contRepo = new ContestantDBRepository(serverProps);
        IParticipationRepository partRepo = new ParticipationDBRepository(serverProps);
        ISwimmingRaceRepository swimRepo = new SwimmingRaceDBRepository(serverProps);
        IService contestService = new ContestServiceImpl(adminRepo, contRepo, partRepo, swimRepo);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("contest.server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong  Port Number" + e.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        AbstractServer server = new ContestRpcConcurrentServer(serverPort, contestService);

        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                close();
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }

}
