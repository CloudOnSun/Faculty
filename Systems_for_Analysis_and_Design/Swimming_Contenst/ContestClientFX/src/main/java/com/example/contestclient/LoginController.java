package com.example.contestclient;

import concurs.domain.Admin;
import concurs.domain.AdminLogInDTO;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.WindowEvent;
import service.ContestException;
import service.IService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginController {
    @FXML
    private Button button_login;
    @FXML
    private PasswordField textfield_pswd;
    @FXML
    private TextField textfield_email;

    IService srv;
    private MainPageController mainPageController;
    Parent mainControllerParent;

    public void setService(IService srv) {
        this.srv = srv;
    }
    public void setMainController(MainPageController mainController) {
        this.mainPageController = mainController;
    }

    public void setParent(Parent p){
        mainControllerParent=p;
    }


    public void onLogIn(ActionEvent actionEvent) throws IOException {
        String email = textfield_email.getText();
        String paswd = textfield_pswd.getText();

        if (email.isEmpty() || paswd.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
        else {
            try {
                Admin admin = srv.getAccount(new AdminLogInDTO(email, paswd), mainPageController);

                Stage stage = new Stage();
                stage.setTitle("Administrator: " + admin.getEmail());
                stage.setScene(new Scene(mainControllerParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        mainPageController.logout();
                        System.exit(0);
                    }
                });
                stage.show();
                mainPageController.setAdmin(admin);
                mainPageController.initialize(srv);

                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            } catch (ContestException e) {
                Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        }
    }
}
