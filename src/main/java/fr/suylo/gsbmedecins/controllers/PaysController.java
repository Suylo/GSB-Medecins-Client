package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaysController implements Initializable {

    @FXML
    public TableView<Pays> tableView;
    public TableColumn<Pays, String> nom;
    public TableColumn<Pays, Long> id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
