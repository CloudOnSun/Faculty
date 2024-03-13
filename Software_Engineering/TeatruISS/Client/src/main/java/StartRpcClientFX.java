import Florea_Flaviu_ISS.controllers.FirstPageController;
import Florea_Flaviu_ISS.controllers.LogInPageController;
import Florea_Flaviu_ISS.controllers.ManagerPageController;
import Florea_Flaviu_ISS.controllers.SpectacolController;
import Florea_Flaviu_ISS.rpcprotocol.ServerRpcProxy;
import Florea_Flaviu_ISS.service.IService;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX  extends Application {

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
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

        IService server = new ServerRpcProxy(serverIP, serverPort);


        FXMLLoader loaderFirstPage = new FXMLLoader(
                StartRpcClientFX.class.getResource("/firstPage.fxml"));
        Parent rootFirstPage=loaderFirstPage.load();

        FirstPageController firstPgCtrl =
                loaderFirstPage.<FirstPageController>getController();
        firstPgCtrl.setService(server);


        FXMLLoader loaderLogIn = new FXMLLoader(
                StartRpcClientFX.class.getResource("/logInPage.fxml"));
        Parent rootLogInPage=loaderLogIn.load();

        LogInPageController logInCtrl =
                loaderLogIn.<LogInPageController>getController();
        logInCtrl.setService(server);


        FXMLLoader loaderManagerPage = new FXMLLoader(
                StartRpcClientFX.class.getResource("/managerPage.fxml"));
        Parent rootManagerPage = loaderManagerPage.load();

        ManagerPageController managerPageController =
                loaderManagerPage.<ManagerPageController>getController();
        managerPageController.setService(server);

        FXMLLoader loaderSpectacolPage = new FXMLLoader(
                StartRpcClientFX.class.getResource("/spectacolPage.fxml"));
        Parent rootSpectacolPage = loaderSpectacolPage.load();

        SpectacolController spectacolController =
                loaderSpectacolPage.<SpectacolController>getController();
        spectacolController.setSrv(server);


        firstPgCtrl.setLogInParent(rootLogInPage);
        firstPgCtrl.fillComboBoxGenuri();
        firstPgCtrl.fillTableSpectacole();
        firstPgCtrl.setManagerPageController(managerPageController);
        firstPgCtrl.setSpectacolController(spectacolController);
        firstPgCtrl.setSpectacolParent(rootSpectacolPage);

        logInCtrl.setManagerPageController(managerPageController);
        logInCtrl.setFirstPageController(firstPgCtrl);
        logInCtrl.setManagerParent(rootManagerPage);

        managerPageController.setFirstPageController(firstPgCtrl);

        primaryStage.setTitle("Spectacole la teatru");
        primaryStage.setScene(new Scene(rootFirstPage, 920, 400));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                firstPgCtrl.logOut();
                System.exit(0);
            }
        });


    }
}
