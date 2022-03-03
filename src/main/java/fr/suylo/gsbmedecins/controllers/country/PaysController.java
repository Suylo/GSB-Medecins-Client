package fr.suylo.gsbmedecins.controllers.country;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Pays;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaysController implements Initializable {

    @FXML
    public TableView<Pays> tableView;
    public TableColumn<Pays, String> nom;
    public TableColumn<Pays, Long> id;
    public Label titlePays, titleBlank;
    public final Button addPays = new Button("Ajouter un pays");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addPays.getStyleClass().add("button-see");
        if(UserSession.getUserLoggedOn()){
            titleBlank.setGraphic(addPays);
            addPays.setAlignment(Pos.BOTTOM_CENTER);
            addPays.setOnAction(event -> {
                Pane addCountry = null;
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addPays.fxml"));
                try {
                    addCountry = loader.load();
                    addCountry.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CustomStage stage = ((CustomStage) addPays.getScene().getWindow());
                stage.setTitle("GSB - Ajout d'un pays");
                stage.changeScene(addCountry);
            });
        }

        loadCountries();
    }

    private void loadCountries() {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/pays").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération au format Json de tous les médecins
        Pays[] paysJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Pays[].class);

        // Création d'une collection pour les passer en objet
        ObservableList<Pays> data = FXCollections.observableArrayList();
        for (Pays pays: paysJson) {
            data.addAll(
                    new Pays(
                            pays.getId(),
                            pays.getNom(),
                            pays.getDepartements()
                    )
            );
        }

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        tableView.getItems().addAll(data);
        // Parcours sur chaque boucles
//        for (Pays pays : data) {
//            ArrayList<TitledPane> titledPanes = new ArrayList<>();
//            ListView<String> listView = new ListView();
//            titledPanes.add(new TitledPane(pays.getNom(), listView));
//            accordion.getPanes().addAll(titledPanes);
//            for (Departement departement : pays.getDepartements()) {
//                listView.getItems().add(departement.getNom());
//                for (Medecin medecin : departement.getMedecins()) {
//                }
//            }
//        }
    }
}