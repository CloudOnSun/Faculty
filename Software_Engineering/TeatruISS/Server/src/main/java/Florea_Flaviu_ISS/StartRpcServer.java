package Florea_Flaviu_ISS;

import Florea_Flaviu_ISS.domain.*;
import Florea_Flaviu_ISS.repository.*;
import Florea_Flaviu_ISS.server.TeatruServiceImpl;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.utils.AbstractServer;
import Florea_Flaviu_ISS.utils.ContestRpcConcurrentServer;
import Florea_Flaviu_ISS.utils.ServerException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Properties;

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
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties + " + e);
            return;
        }

        IManagerRepository managerRepository = new ManagerHibernateRepository(sessionFactory);
        IDateTimeRepository dateTimeRepository = new DateTimeHibernateRepository(sessionFactory);
        ISpectacolRepository spectacolRepository = new SpectacolHibernateRepository(sessionFactory);
        ILocRepository locRepository = new LocHibernateRepository(sessionFactory);
        ISpectatorRepository spectatorRepository = new SpectatorHibernateRepository(sessionFactory);
        IRezervareRepository rezervareRepository = new RezervareHibernateRepository(sessionFactory);
        IService contestService = new TeatruServiceImpl(managerRepository, dateTimeRepository, spectacolRepository, locRepository, spectatorRepository, rezervareRepository);

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