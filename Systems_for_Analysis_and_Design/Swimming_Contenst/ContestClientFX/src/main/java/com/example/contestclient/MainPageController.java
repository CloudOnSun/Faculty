package com.example.contestclient;

import concurs.domain.*;
//import domain.*;
import javafx.application.Platform;
import service.ContestException;
import service.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.IServiceObserver;

import java.util.ArrayList;
import java.util.List;

public class MainPageController implements IServiceObserver {

    @FXML
    private Button buton_logout;
    @FXML
    private TextField textfield_varsta;
    @FXML
    private TextField textfield_nume;
    @FXML
    private TableColumn col_races_part;
    @FXML
    private TextField textfield_distance;
    @FXML
    private TextField textfield_style;
    @FXML
    private TableColumn col_varsta_part;
    @FXML
    private TableColumn col_nume_part;
    @FXML
    private TableView table_cauta;
    @FXML
    private TableView table_nrPart_race;
    @FXML
    private TableColumn col_distance;
    @FXML
    private TableColumn col_stil;
    @FXML
    private TableColumn col_nrCont;

    private Admin admin;
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    private IService srv;
    public void setService(IService srv) {
        this.srv = srv;
    }

    private final ObservableList<RaceNrContestantsDTO> partDTOModel = FXCollections.observableArrayList();
    private final ObservableList<ContestantWithRacesDTO> searchContest = FXCollections.observableArrayList();


    public void initialize(IService server) {
        this.srv = server;

        try {
            var raceNrContestList = srv.nrContestantEachRace(this);

            partDTOModel.setAll(raceNrContestList.getRaceNrContestantsDTOList());
            col_distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
            col_stil.setCellValueFactory(new PropertyValueFactory<>("style"));
            col_nrCont.setCellValueFactory(new PropertyValueFactory<>("nrContestants"));

            table_nrPart_race.setItems(partDTOModel);

            table_nrPart_race.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        catch (ContestException e) {
            e.printStackTrace();
        }
    }


    public void onClickCauta(ActionEvent actionEvent) {
        String distance = textfield_distance.getText();
        String style = textfield_style.getText();

        if (distance.isEmpty() || style.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        } else {
            try {
                var contestants = srv.getByRace(new RaceDTO(distance, style), this);

                searchContest.setAll(contestants.getContestantWithRacesDTOList());
                col_nume_part.setCellValueFactory(new PropertyValueFactory<>("name"));
                col_varsta_part.setCellValueFactory(new PropertyValueFactory<>("age"));
                col_races_part.setCellValueFactory(new PropertyValueFactory<>("races"));

                table_cauta.setItems(searchContest);
            } catch (ContestException e) {
                Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        }

    }

    public void onClickAdauga(ActionEvent actionEvent) {
        String name = textfield_nume.getText();
        try {
            Integer age = Integer.parseInt(textfield_varsta.getText());
            List<RaceNrContestantsDTO> racesDTO = new ArrayList<>(table_nrPart_race.getSelectionModel().getSelectedItems());
            if (name.isEmpty() || age < 5 || age > 50 || racesDTO.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            } else {
                try {
                    List<SwimmingRace> races = new ArrayList<>();
                    for (var race : racesDTO) {
                        races.add(srv.findByDistanceAndStyle(new RaceDTO(race.getDistance(), race.getStyle()), this));
                    }
                    srv.addContestantToRaces(new ContestantDTO(name, age, races), this);
                } catch (ContestException e) {
                    Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                    alert.setTitle("ERROR");
                    alert.show();
                }
            }

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
    }

    public void logout() {
        try {
            srv.logOut(admin, this);
        } catch (ContestException e) {
            e.printStackTrace();
        }
    }

    public void onClick_logout(ActionEvent actionEvent) {
        try {
            logout();
            Stage stage = (Stage) buton_logout.getScene().getWindow();
            stage.close();
        } catch (Exception ex) {

        }
    }

    @Override
    public void updateContestants() throws ContestException {
        Platform.runLater(() -> initialize(srv));
    }
}
