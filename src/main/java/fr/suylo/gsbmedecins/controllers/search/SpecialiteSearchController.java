package fr.suylo.gsbmedecins.controllers.search;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.ProfileController;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SpecialiteSearchController implements Initializable {


    public TableView<Medecin> myTable;
    public MenuButton speChoice;
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

    private String speciality;
    @FXML
    private ComboBox<String> selectSpecialities;


    public void searchSpeciality() {
        searchEnter.setOnAction(event -> {
            this.speciality = selectSpecialities.getValue().replace(" ", "+");

            HttpResponse<JsonNode> apiResponse = null;
            try {
                apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins/specialite?spe=" + this.speciality).asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            Medecin[] medecinsByNomOrPrenom = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);

            ObservableList<Medecin> data = FXCollections.observableArrayList();
            for (Medecin medecin : medecinsByNomOrPrenom) {
                data.addAll(new Medecin(medecin.getId(), medecin.getNom(), medecin.getPrenom(), medecin.getAdresse(), medecin.getTel(), medecin.getSpe()));
            }

            myTable.getItems().clear();
            for (Medecin medecin : data) {
                myTable.getItems().add(medecin);
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
        loadSpecialities();
        searchSpeciality();
        // Temporaire en attendant de trouver une autre solution
        searchByDepartment.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/departement-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchByDepartment.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin par département");
            stage.changeScene(searchStage);
        });
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

    private void loadSpecialities() {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins/").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Medecin[] medecinsBySpecialite = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);

        ObservableSet<String> listSpe = FXCollections.observableSet();
        for (Medecin medecin : medecinsBySpecialite) {
            if (medecin.getSpe() != null){
                listSpe.add(medecin.getSpe());
            }
        }
        System.out.println(listSpe);
        selectSpecialities.getItems().addAll(listSpe);
    }
}
