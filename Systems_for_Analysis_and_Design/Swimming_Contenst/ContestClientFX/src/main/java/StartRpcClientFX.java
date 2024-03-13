import com.example.contestclient.LoginController;

import com.example.contestclient.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rpcprotocols.ContestServicesProtoProxy;
import rpcprotocols.ContestServicesRpcProxy;
import service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {

    private Stage primaryStage;

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();

        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("contest.server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("contest.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        //IService server = new ContestServicesProtoProxy(serverIP, serverPort);
        IService server = new ContestServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                StartRpcClientFX.class.getResource("/login.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setService(server);

        FXMLLoader cloader = new FXMLLoader(
                StartRpcClientFX.class.getResource("/main_page.fxml"));
        Parent croot=cloader.load();


        MainPageController mainCtrl =
                cloader.<MainPageController>getController();
        mainCtrl.setService(server);

        ctrl.setMainController(mainCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("Log in");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

    }

}
