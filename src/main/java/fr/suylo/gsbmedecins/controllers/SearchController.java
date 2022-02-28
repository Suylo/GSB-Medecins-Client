package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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

public class SearchController implements Initializable {

    @FXML
    public Button searchByCountry, searchByDepartment, searchBySpeciality, searchByFLName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

}
