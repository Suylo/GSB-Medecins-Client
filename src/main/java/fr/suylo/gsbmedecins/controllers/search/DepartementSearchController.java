package fr.suylo.gsbmedecins.controllers.search;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.ProfileController;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DepartementSearchController implements Initializable {


    public TableView<Medecin> myTable;
    @FXML
    public Button searchEnter;
    @FXML
    public TableColumn<Medecin, Integer> id;
    @FXML
    public TableColumn<Medecin, String> nomCol, prenomCol, spe;
    @FXML
    public TableColumn<Medecin, Medecin> action = new TableColumn<>("Action");
    @FXML
    public Button searchByCountry, searchByDepartment, searchBySpeciality, searchByFLName;
    @FXML
    public ComboBox<String> selectDepartments;

    private String nomDepartement;

    public void searchDepartments() {
        searchEnter.setOnAction(event -> {
            this.nomDepartement = selectDepartments.getValue();

            HttpResponse<JsonNode> apiResponse = null;
            try {
                apiResponse = Unirest.get("http://localhost:8080/api/v1/departements/nom?nom=" + this.nomDepartement).asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            Departement[] lesDepartements = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Departement[].class);

            ObservableList<Departement> data = FXCollections.observableArrayList();
            for (Departement departement : lesDepartements) {
                data.addAll(
                        new Departement(
                                departement.getId(),
                                departement.getNom(),
                                departement.getMedecins()
                        )
                );
            }

            myTable.getItems().clear();
            for (Departement departement: data) {
                for (Medecin medecin : departement.getMedecins()) {
                    myTable.getItems().add(medecin);
                }
            }
            if (data.get(0).getMedecins().isEmpty()){
                myTable.setPlaceholder(new Label("Aucun médecin n'existe pour ce département !"));
            }

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            spe.setCellValueFactory(new PropertyValueFactory<>("spe"));
        });

        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> new TableCell<>() {
            public final Button seeButton = new Button("Voir");

            protected void updateItem(Medecin person, boolean empty) {
                super.updateItem(person, empty);
                setGraphic(seeButton);
                seeButton.getStyleClass().add("button-see");
                int index = Integer.parseInt(String.valueOf(getTableRow().getIndex()));
                seeButton.setOnAction(event -> {
                    Pane doctor = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctorInfo.fxml"));
                    try {
                        doctor = loader.load();
                        doctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ProfileController profileController = loader.getController();
                    profileController.loadData(id.getCellData(index));
                    profileController.loadProfile();
                    CustomStage stage = ((CustomStage) seeButton.getScene().getWindow());
                    stage.setTitle("GSB - Profil d'un médecin ");
                    stage.changeScene(doctor);
                });
            }
        });
        myTable.setPlaceholder(new Label("Veuillez commencer votre recherche !"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadDepartments();

        searchDepartments();

        // Temporaire en attendant de trouver une autre solution
        searchByCountry.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/pays-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchByCountry.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin par pays");
            stage.changeScene(searchStage);
        });
        searchBySpeciality.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/specialite-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchBySpeciality.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin par pays");
            stage.changeScene(searchStage);
        });
        searchByFLName.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/nomprenom-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchByFLName.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin");
            stage.changeScene(searchStage);
        });
    }

    private void loadDepartments() {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/departements").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Departement[] lesDepartements = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Departement[].class);

        ObservableList<Departement> data = FXCollections.observableArrayList();
        for (Departement unDepartement : lesDepartements) {
            data.addAll(
                    new Departement(
                            unDepartement.getId(),
                            unDepartement.getNom(),
                            unDepartement.getMedecins()
            ));
        }

        for (Departement departement : data) {
            selectDepartments.getItems().add(departement.getNom());
        }

    }
}
