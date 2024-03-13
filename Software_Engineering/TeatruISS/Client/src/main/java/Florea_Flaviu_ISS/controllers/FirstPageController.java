package Florea_Flaviu_ISS.controllers;

import Florea_Flaviu_ISS.domain.*;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.service.IServiceObserver;
import Florea_Flaviu_ISS.service.TeatruException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstPageController implements IServiceObserver {
    @FXML
    private TextField codRezervareField;
    @FXML
    private Button butonStergeRez;
    @FXML
    private TableView spectacoleTable;
    @FXML
    private TableColumn titlu;
    @FXML
    private TableColumn regizor;
    @FXML
    private TableColumn zi;
    @FXML
    private TableColumn luna;
    @FXML
    private TableColumn an;
    @FXML
    private TableColumn ora;
    @FXML
    private TableColumn gen;
    @FXML
    private TableColumn minut;
    @FXML
    private TableColumn teatru;
    @FXML
    private Button autentificare;
    @FXML
    private ComboBox genFiltru;

    private  Manager manager = null;
    private SpectacolController spectacolController;
    private Parent rootSpectacolPage;

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    Teatru teatruGeneral;

    public Teatru getTeatruGeneral() {
        return teatruGeneral;
    }

    public void setTeatruGeneral(Teatru teatruGeneral) {
        this.teatruGeneral = teatruGeneral;
    }

    IService srv;

    public void setService(IService srv) {
        this.srv = srv;
    }

    Parent logInParent;


    ManagerPageController managerPageController;

    public ManagerPageController getManagerPageController() {
        return managerPageController;
    }

    public void setManagerPageController(ManagerPageController managerPageController) {
        this.managerPageController = managerPageController;
    }

    public void setLogInParent(Parent logInParent) {
        this.logInParent = logInParent;
    }
    private final ObservableList<SpectacolDTOTableView> obsvListSpecs = FXCollections.observableArrayList();
    private final ObservableList<Genuri> obsvListGenuri = FXCollections.observableArrayList();
    List<Spectacol> spectacolList = new ArrayList<>();
    List<SpectacolDTOTableView> spectacolDTOTableViewsList = new ArrayList<>();

    public void fillTableSpectacole() {
        try {
            spectacolDTOTableViewsList.clear();
            Genuri genFltr = null;
            if (genFiltru.getValue() != null)
                genFltr = Genuri.valueOf(genFiltru.getValue().toString());

            spectacolList = srv.findAllSpectacole(this);
            for( var s : spectacolList) {
                teatruGeneral = s.getTeatru();
                var s2 = new SpectacolDTOTableView(s.getGen(), s.getRegizor(), s.getTitlu(),
                                s.getDateTime().getYear(), s.getDateTime().getMonth(), s.getDateTime().getDay(),
                                s.getDateTime().getHour(), s.getDateTime().getMinute(), s.getTeatru().getNume());
                s2.setId(s.getId());
                if (genFltr != null) {
                    if (s2.getGen() == genFltr) {
                        spectacolDTOTableViewsList.add(s2);
                    }
                }
                else {
                    spectacolDTOTableViewsList.add(s2);
                }
            }

            obsvListSpecs.setAll(spectacolDTOTableViewsList);
            titlu.setCellValueFactory(new PropertyValueFactory<>("titlu"));
            regizor.setCellValueFactory(new PropertyValueFactory<>("regizor"));
            gen.setCellValueFactory(new PropertyValueFactory<>("gen"));
            an.setCellValueFactory(new PropertyValueFactory<>("year"));
            luna.setCellValueFactory(new PropertyValueFactory<>("month"));
            zi.setCellValueFactory(new PropertyValueFactory<>("day"));
            ora.setCellValueFactory(new PropertyValueFactory<>("hour"));
            minut.setCellValueFactory(new PropertyValueFactory<>("minute"));
            teatru.setCellValueFactory(new PropertyValueFactory<>("teatru"));

            spectacoleTable.setItems(obsvListSpecs);
        }
        catch (TeatruException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
    }

    public void fillComboBoxGenuri() {

        obsvListGenuri.setAll(Arrays.stream(Genuri.values()).toList());
        genFiltru.setItems(obsvListGenuri);
    }

    @Override
    public void updateLocuri() throws TeatruException {

    }

    @Override
    public void updateSpectacole() throws TeatruException {
        Platform.runLater(this::fillTableSpectacole);
    }

    public void onActionFiltruGen(ActionEvent actionEvent) {
        fillTableSpectacole();
    }

    public void clickButonAutentificare(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Log in");
        stage.setScene(new Scene(logInParent));
        stage.show();

    }

    public void logOut() {
        if (manager != null) {
            managerPageController.logOut();
        }
    }

    public void setSpectacolController(SpectacolController spectacolController) {
        this.spectacolController = spectacolController;
    }

    public void onRezerva(ActionEvent actionEvent) {
        SpectacolDTOTableView selectedSpec = (SpectacolDTOTableView) spectacoleTable.getSelectionModel().getSelectedItem();
        try {
            var spectacole = srv.findAllSpectacole(this);
            Spectacol spectacol = null;
            for (var s : spectacole) {
                if (s.getId() == selectedSpec.getId()) {
                    spectacol = s;
                    break;
                }
            }

            spectacolController.setSpectacol(spectacol);
            Stage stage = new Stage();
            stage.setTitle(spectacol.getTitlu());
            stage.setScene(new Scene(rootSpectacolPage));
            stage.show();
            spectacolController.initializeaza();
        } catch (TeatruException e) {
            throw new RuntimeException(e);
        }

    }

    public void setSpectacolParent(Parent rootSpectacolPage) {
        this.rootSpectacolPage = rootSpectacolPage;
    }

    public void onStergeRezervare(ActionEvent actionEvent) {
        try {
            Integer cod = Integer.parseInt(codRezervareField.getText());

            try {
                srv.deleteRezervare(cod, this);
                Alert alert = new Alert(Alert.AlertType.NONE, "A-ti anulat rezervarea", ButtonType.OK);
                alert.setTitle("INFO");
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
}
