package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DepartementController implements Initializable {

    @FXML
    public TableView<Departement> tableView;
    @FXML
    public TableColumn<Departement, Integer> id;
    @FXML
    public TableColumn<Departement, String> nom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDepartments();
    }

    private void loadDepartments() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/departements").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération au format Json de tous les médecins
        Departement[] departementJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Departement[].class);

        // Création d'une collection pour les passer en objet
        ObservableList<Departement> data = FXCollections.observableArrayList();
        for (Departement departement : departementJson) {
            data.addAll(
                    new Departement(
                            departement.getId(),
                            departement.getNom(),
                            departement.getMedecins()
                    )
            );
        }

        tableView.getItems().addAll(data);
    }
}
