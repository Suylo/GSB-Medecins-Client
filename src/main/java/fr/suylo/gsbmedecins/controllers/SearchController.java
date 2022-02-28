package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Medecin;
import fr.suylo.gsbmedecins.models.UserSession;
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

    private String nom;
    private String prenom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchDoctor();
    }

    public void searchDoctor() {
        searchEnter.setOnAction(event -> {
            // On enlève les espaces pour éviter des erreurs
            this.prenom = textField.getText().trim();
            this.nom = textField.getText().trim();

            // Requête HTTP
            HttpResponse<JsonNode> apiResponse = null;
            try {
                // Recherche au niveau de l'API pour savoir si il y a un nom/prenom qui correspond
                apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins/search?nom=" + this.getNom() + "&prenom=" + this.getPrenom()).asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            // Récupération au format Json
            Medecin[] medecinsByNomOrPrenom = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);

            ObservableList<Medecin> data = FXCollections.observableArrayList();
            for (Medecin medecin : medecinsByNomOrPrenom) {
                data.addAll(new Medecin(medecin.getId(), medecin.getNom(), medecin.getPrenom(), medecin.getAdresse(), medecin.getTel(), medecin.getSpe()));
            }

            // On clear la table et ensuite on ajoute les médecins
            myTable.getItems().clear();
            for (Medecin medecin : data) {
                myTable.getItems().add(medecin);
            }

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            spe.setCellValueFactory(new PropertyValueFactory<>("spe"));
        });


        // Affectation des différents champs
        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> new TableCell<>() {
            public final Button seeButton = new Button("Voir");

            // Update
            protected void updateItem(Medecin person, boolean empty) {
                super.updateItem(person, empty);

                // On affiche le button dans la table
                setGraphic(seeButton);
                // On ajoute du css à ce button
                seeButton.getStyleClass().add("button-see");

                // On récupère le numéro de la ligne pour pouvoir set un id à un button, exemple ligne 5 = button ID 5
                int index = Integer.parseInt(String.valueOf(getTableRow().getIndex()));

                // Quand on clique sur le bouton
                seeButton.setOnAction(event -> {

                    // On charge la vue FXML des infos des docteurs
                    Pane doctor = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctorInfo.fxml"));
                    try {
                        doctor = loader.load();
                        doctor.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // On init le controller Profile
                    ProfileController profileController = loader.getController();
                    // Une fois le controller initialisé on envoie une variable
                    profileController.loadData(id.getCellData(index));
                    // On load une fonction qui récupère la variable d'avant
                    profileController.loadProfile();

                    // Chargement de la scène avec CustomStage
                    CustomStage stage = ((CustomStage) seeButton.getScene().getWindow());
                    stage.setTitle("GSB - Profil d'un médecin ");
                    stage.changeScene(doctor);

                });
            }
        });

        // Erreur de connexion avec l'API
        myTable.setPlaceholder(new Label("Veuillez commencer votre recherche !"));
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
