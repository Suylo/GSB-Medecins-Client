package fr.suylo.gsbmedecins.controllers.search;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.controllers.ProfileController;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Medecin;
import fr.suylo.gsbmedecins.models.Pays;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class PaysSearchController implements Initializable {


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
    public TextField textField;
    @FXML
    public Button searchByCountry, searchByDepartment, searchBySpeciality, searchByFLName;

    @FXML
    public ComboBox<String> selectCountries;
    private String listCountries;

    public void searchCountries() {
        searchEnter.setOnAction(event -> {
            this.listCountries = selectCountries.getValue();

            HttpResponse<JsonNode> apiResponse = null;
            try {
                apiResponse = Unirest.get("http://localhost:8080/api/v1/pays/nom?nom=" + this.listCountries).asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            Pays[] tousLesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Pays[].class);

            ObservableList<Pays> data = FXCollections.observableArrayList();
            for (Pays pays: tousLesPays) {
                data.addAll(
                        new Pays(
                                pays.getId(),
                                pays.getNom(),
                                pays.getDepartements()
                        )
                );
            }

            myTable.getItems().clear();
            for (Pays pays : data) {
                for (Departement dep : pays.getDepartements()) {
                    for (Medecin med : dep.getMedecins()) {
                        myTable.getItems().add(med);
                    }
                }
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

        loadCountries();

        searchCountries();

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

    private void loadCountries() {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/pays").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Pays[] lesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Pays[].class);

        ObservableList<Pays> data = FXCollections.observableArrayList();
        for (Pays unPays : lesPays) {
            data.addAll(
                    new Pays(
                            unPays.getId(),
                            unPays.getNom(),
                            unPays.getDepartements()
                    ));
        }

        for (Pays unPays : data) {
            selectCountries.getItems().add(unPays.getNom());
        }

    }

}
