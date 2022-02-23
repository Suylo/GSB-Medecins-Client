package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.User;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable{


    @FXML
    public TextField loginPassword;

    @FXML
    public TextField loginID;

    @FXML
    public Button buttonSubmit;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Connexion à l'API
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/users").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        User[] usersJson = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), User[].class);

        ObservableList<User> data = FXCollections.observableArrayList();
        for (User user : usersJson) {
            data.addAll(new User(user.getId(), user.getLogin(), user.getNom(), user.getPrenom(), user.getMotdepasse(), user.getAdresse(), user.getEmbauche()));
        }

        buttonSubmit.setOnAction(event -> {

            // Récupération des textes des champs
            String login = loginID.getText();
            String mdp = loginPassword.getText();

            // Ptit débug
            System.out.println("Login : " + login + " | Mdp : " + mdp);

            // Check si d'abord les champs ne sont pas vides
            if (!login.isEmpty() && !mdp.isEmpty()){
                // On parcontre tous les utilisateurs
                for (User user : data) {
                    // Check si le login et mdp donnés correspondent aux utilisateurs
                    if (user.getLogin().equals(login) && user.getMotdepasse().equals(mdp)){
                        // Exécution de la fonction connexion(identifiant, motdepasse)
                        connexion(login, mdp);
                        // On sort de la boucle
                        break;
                    }
                }
            } else {
                // Les champs ne sont pas complétés : on affiche une erreur indiquant de compléter les champs
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Champs incomplets !");
                alert.setHeaderText("Les champs ne peuvent être vides !");
                alert.setContentText("Veuillez réessayer.");
                alert.show();
            }
        });

    }

    private void connexion(String login, String passwd){
        // Connexion de l'utilisateur, stockage d'une variable de "session"
        UserSession.getInstace(login, passwd);
        // Debug pour vérifier que les variables de "session" sont réutilisables
        System.out.println("LoginController :: User ID : " + UserSession.getUserID());
        System.out.println("LoginController :: User Passwd : " + UserSession.getUserPassword());

        // Check si les deux variables ne sont pas vides, création d'une autre scène
        if(UserSession.getUserLoggedOn()){
            Stage stage1 = (Stage) buttonSubmit.getScene().getWindow();
            try {
                // On récupère la position de l'ancienne fenêtre pour plus d'UX
                double x = stage1.getX();
                double y = stage1.getY();
                // Chargement de la scène d'administration
                UserSession.loadUi(x, y);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // On ferme l'ancienne fenêtre après avoir chargée la nouvelle
            stage1.close();
        }
    }

}
