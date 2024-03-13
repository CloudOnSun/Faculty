package Florea_Flaviu_ISS.controllers;

import Florea_Flaviu_ISS.domain.*;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.service.TeatruException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManagerPageController {

    @FXML
    private TextField idField;
    @FXML
    private TextField genField;
    @FXML
    private TextField ziField;
    @FXML
    private TextField lunaField;
    @FXML
    private TextField anField;
    @FXML
    private TextField oraField;
    @FXML
    private TextField minutField;
    @FXML
    private TextField titluField;
    @FXML
    private TextField regizorField;
    @FXML
    private Button butonAdauga;
    @FXML
    private Button butonSterge;
    @FXML
    private Button butonUpdate;

    Teatru teatru;

    IService srv;


    public void setService(IService srv) {
        this.srv = srv;
    }

    Manager manager;
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    FirstPageController firstPageController;

    public FirstPageController getFirstPageController() {
        return firstPageController;
    }

    public void setFirstPageController(FirstPageController firstPageController) {
        this.firstPageController = firstPageController;
    }

    public void logOut() {
        try {
            srv.logOut(manager, firstPageController);
            firstPageController.setManager(null);
        } catch (TeatruException e) {
            e.printStackTrace();
        }
    }

    public void onClickAdaugaSpectacol(ActionEvent actionEvent) {
        try {
            String titlu = titluField.getText();
            String regizor = regizorField.getText();
            Integer zi = Integer.parseInt(ziField.getText());
            Integer luna = Integer.parseInt(lunaField.getText());
            Integer an = Integer.parseInt(anField.getText());
            Integer ora = Integer.parseInt(oraField.getText());
            Integer minut = Integer.parseInt(minutField.getText());
            Genuri gen = Genuri.valueOf(genField.getText());

            SpectacolDTO specDTO = new SpectacolDTO(
                    gen, regizor, titlu, an, luna, zi, ora, minut, manager, teatru
            );
            try {
                srv.addSpectacol(specDTO, firstPageController);
            } catch (TeatruException ex) {
                Alert alert = new Alert(Alert.AlertType.NONE, ex.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.NONE, "NU da string pentru int", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }

    }

    public void setTeatru(Teatru teatruGeneral) {
        this.teatru = teatruGeneral;
    }

    public void onStergeSpectacol(ActionEvent actionEvent) {
        try {
            Integer idSpectacol = Integer.parseInt(idField.getText());
            try {
                srv.deleteSpectacol(idSpectacol, firstPageController);
            } catch (TeatruException ex) {
                Alert alert = new Alert(Alert.AlertType.NONE, ex.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.NONE, "NU da string pentru int", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
    }

    public void onUpdateSpectacol(ActionEvent actionEvent) {
        try {
            Integer idSpectacol = Integer.parseInt(idField.getText());
            String titlu = titluField.getText();
            String regizor = regizorField.getText();
            Integer zi = Integer.parseInt(ziField.getText());
            Integer luna = Integer.parseInt(lunaField.getText());
            Integer an = Integer.parseInt(anField.getText());
            Integer ora = Integer.parseInt(oraField.getText());
            Integer minut = Integer.parseInt(minutField.getText());
            Genuri gen = Genuri.valueOf(genField.getText());

            SpectacolUpdateDTO specDTO = new SpectacolUpdateDTO(
                    idSpectacol, gen, regizor, titlu, an, luna, zi, ora, minut, manager, teatru
            );
            try {
                srv.updateSpectacol(specDTO, firstPageController);
            } catch (TeatruException ex) {
                Alert alert = new Alert(Alert.AlertType.NONE, ex.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.NONE, "NU da string pentru int", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
    }
}
