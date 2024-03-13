package Florea_Flaviu_ISS.controllers;

import Florea_Flaviu_ISS.domain.*;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.service.IServiceObserver;
import Florea_Flaviu_ISS.service.TeatruException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpectacolController implements IServiceObserver {
    @FXML
    private CheckBox r11;
    @FXML
    private CheckBox r12;
    @FXML
    private CheckBox r13;
    @FXML
    private CheckBox r21;
    @FXML
    private CheckBox r22;
    @FXML
    private CheckBox r23;
    @FXML
    private CheckBox l11;
    @FXML
    private CheckBox l12;
    @FXML
    private CheckBox l21;
    @FXML
    private CheckBox l22;
    @FXML
    private TextField numeField;
    @FXML
    private TextField prenumeField;
    @FXML
    private TextField emailField;
    @FXML
    private Button butonRezerva;

    private CheckBox[] checkBoxes = new CheckBox[]{r11, r12, r13, r21, r22, r23, l11, l12, l21, l22};


    private List<Loc> locuri;

    public IService getSrv() {
        return srv;
    }

    public void setSrv(IService srv) {
        this.srv = srv;
    }

    private IService srv;

    private Spectacol spectacol;

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    public void initializeaza() {
        try {
            checkBoxes = new CheckBox[]{r11, r12, r13, r21, r22, r23, l11, l12, l21, l22};
            locuri = srv.getLocuriSpectacol(this.spectacol, this);
            for (var loc : locuri) {
                if (loc.getRezervare() != null) {
                    String text = "";
                    if (loc.getLoja() != -1)
                        text += "L" + loc.getLoja() + ".";
                    if (loc.getRand() != -1)
                        text += "R" + loc.getRand() + ".";
                    text += loc.getLoc();
                    for (var checkBox : checkBoxes) {
                        if (text.equals(checkBox.getText())) {
                            checkBox.setSelected(false);
                            checkBox.setDisable(true);
                            break;
                        }
                    }
                }
                else {
                    String text = "";
                    if (loc.getLoja() != -1)
                        text += "L" + loc.getLoja() + ".";
                    if (loc.getRand() != -1)
                        text += "R" + loc.getRand() + ".";
                    text += loc.getLoc();
                    for (var checkBox : checkBoxes) {
                        if (text.equals(checkBox.getText())) {
                            checkBox.setDisable(false);
                            break;
                        }
                    }
                }
            }
        } catch (TeatruException e) {
            throw new RuntimeException(e);
        }
    }


    public void onRezerva(ActionEvent actionEvent) {

        try {
            String nume = numeField.getText();
            String prenume = prenumeField.getText();
            String email = emailField.getText();
            RezervareDTO rezervareDTO = new RezervareDTO(nume, prenume, email, this.spectacol);
            var listLocuri = rezervareDTO.getLocuri();
            checkBoxes = new CheckBox[]{r11, r12, r13, r21, r22, r23, l11, l12, l21, l22};
            for (var box :checkBoxes) {
                if (box.isSelected()) {
                    String textBox = box.getText();
                    for (var loc : locuri) {
                        String text = "";
                        if (loc.getLoja() != -1)
                            text += "L" + loc.getLoja() + ".";
                        if (loc.getRand() != -1)
                            text += "R" + loc.getRand() + ".";
                        text += loc.getLoc();
                        if (textBox.equals(text)) {
                            listLocuri.add(loc);
                            break;
                        }
                }
                }
            }

            try {
                Integer code = srv.addRezervare(rezervareDTO, this);
                String msg = "Codul dumneavoastra de rezervare este: " + code + ". NU IL UITATI!!!";
                Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
                alert.setTitle("REZERVARE EFECTUATA CU SUCCES!");
                alert.show();
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

    @Override
    public void updateLocuri() throws TeatruException {
        Platform.runLater(this::initializeaza);
    }

    @Override
    public void updateSpectacole() throws TeatruException {

    }
}
