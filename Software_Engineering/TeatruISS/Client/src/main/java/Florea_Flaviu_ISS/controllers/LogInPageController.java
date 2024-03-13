package Florea_Flaviu_ISS.controllers;

import Florea_Flaviu_ISS.domain.Manager;
import Florea_Flaviu_ISS.domain.ManagerLogInDTO;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.service.TeatruException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LogInPageController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInButton;

    IService srv;

    public void setService(IService srv) {
        this.srv = srv;
    }


    ManagerPageController managerPageController;
    public void setManagerPageController(ManagerPageController managerPageController) {
        this.managerPageController = managerPageController;
    }

    FirstPageController firstPageController;
    public void setFirstPageController(FirstPageController firstPgCtrl) {
        this.firstPageController = firstPgCtrl;
    }

    Parent rootManagerPage;
    public void setManagerParent(Parent rootManagerPage) {
        this.rootManagerPage = rootManagerPage;
    }

    public void onClickLogIn(ActionEvent actionEvent) {
        String email = emailField.getText();
        String paswd = passwordField.getText();

        if (email.isEmpty() || paswd.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
        else {
            try {
                Manager manager = srv.logIn(new ManagerLogInDTO(email, paswd), firstPageController);

                Stage stage = new Stage();
                stage.setTitle("Administrator: " + manager.getEmail());
                stage.setScene(new Scene(rootManagerPage));


                stage.show();
                managerPageController.setManager(manager);
                firstPageController.setManager(manager);

                managerPageController.setTeatru(firstPageController.getTeatruGeneral());

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        managerPageController.logOut();
                    }
                });

                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            } catch (TeatruException e) {
                Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        }
    }

    public void logOut() {
    }
}
