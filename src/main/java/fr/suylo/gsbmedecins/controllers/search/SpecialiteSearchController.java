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

    private String nom;
    private String prenom;


    public void searchDepartments() {
        searchEnter.setOnAction(event -> {
            this.prenom = textField.getText().trim();
            this.nom = textField.getText().trim();

            HttpResponse<JsonNode> apiResponse = null;
            try {
                apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins/search?nom=" + this.nom + "&prenom=" + this.prenom).asJson();
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
        searchDepartments();

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
/*        searchBySpeciality.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("pays-search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchBySpeciality.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin par pays");
            stage.changeScene(searchStage);
        });*/
        searchByFLName.setOnAction(event -> {
            Pane searchStage = null;
            try {
                searchStage = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchScenes/search.fxml")));
                searchStage.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) searchByFLName.getScene().getWindow());
            stage.setTitle("GSB - Recherche d'un médecin");
            stage.changeScene(searchStage);
        });
    }
}
