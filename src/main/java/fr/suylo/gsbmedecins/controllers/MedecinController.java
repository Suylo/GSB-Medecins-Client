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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import lk.vivoxalabs.customstage.CustomStage;
import lk.vivoxalabs.customstage.CustomStageBuilder;
import lk.vivoxalabs.customstage.tools.NavigationType;
import lk.vivoxalabs.customstage.tools.Style;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MedecinController implements Initializable {

    // Déclaration de la table
    @FXML
    public TableView<Medecin> myTable;

    // Déclaration des différents champs relié au FXML
    @FXML
    public TableColumn<Medecin, Integer> id;
    @FXML
    public TableColumn<Medecin, String> nom, prenom, adresse, tel, spe;
    @FXML
    public TableColumn<Medecin, Medecin> action = new TableColumn<>("Action");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        spe.setCellValueFactory(new PropertyValueFactory<>("spe"));

        // Connexion à l'API
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération au format Json de tous les médecins
        Medecin[] medecinsJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin[].class);

        // Création d'une collection pour les passer en objet
        ObservableList<Medecin> data = FXCollections.observableArrayList();
        for (Medecin medecin : medecinsJson) {
            data.addAll(new Medecin(medecin.getId(), medecin.getNom(), medecin.getPrenom(), medecin.getAdresse(), medecin.getTel(), medecin.getSpe()));
        }

        // Ajout de la collection d'objet dans la table
        for (Medecin medecin : data) {
            myTable.getItems().add(medecin);
        }

        action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        action.setCellFactory(param -> {
            return new TableCell<>() {
                public final Button seeButton = new Button("Voir");

                // Update
                protected void updateItem(Medecin person, boolean empty) {
                    super.updateItem(person, empty);

                    // On affiche le button dans la table "Voir" permettant de voir le profil du médecin
                    setGraphic(seeButton);
                    // On ajoute du css à ce button
                    seeButton.getStyleClass().add("button-see");

                    // On récupère le numéro de la ligne pour pouvoir set un id à un button, exemple ligne 5 = button ID 5
                    int index = Integer.parseInt(String.valueOf(getTableRow().getIndex()));
                    // Un peu useless
                    seeButton.setId(String.valueOf(index));

                    // Quand on clique sur le bouton "Voir"
                    seeButton.setOnAction(event -> {
                        // Petit check pour vérif l'état des variables de User"Session"
                        System.out.println("MedecinController :: User ID : " + UserSession.getUserID());
                        System.out.println("MedecinController :: User Passwd : " + UserSession.getUserPassword());

                        // On charge la vue FXML des infos des docteurs
                        Pane doctors = null;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctorInfo.fxml"));
                        try {
                            // Chargement du FXML
                            doctors = loader.load();
                            doctors.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ProfileController profileController = loader.getController();
                        // Une fois le controller initialisé on envoie une variable
                        // On récupère les valeurs de la column avec la variable index
                        profileController.loadData(id.getCellData(index));
                        // On load une fonction qui récupère la variable d'avant
                        profileController.loadProfile();

                        // Chargement de la scène avec CustomStage
                        CustomStage stage = ((CustomStage) seeButton.getScene().getWindow());
                        stage.setTitle("GSB - Profil d'un médecin ");
                        stage.changeScene(doctors);
                    });
                }
            };
        });
        // Erreur de connexion avec l'API
        myTable.setPlaceholder(new Label("Les données n'ont pas pu être récupérée depuis l'API"));
    }
}
