package fr.suylo.gsbmedecins.controllers.admin;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.User;
import fr.suylo.gsbmedecins.models.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    public Label userLastName, userFirstName, userAddress, userEmbauche, userTitle, userID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/users/login?login=" + UserSession.getUserID()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération de l'utilisateur connecté au format Json
        User unAdmin = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), User.class);

        // Ajout des champs du médecin dans les champs FXML
        userID.setText(String.valueOf(unAdmin.getLogin()));
        userLastName.setText(unAdmin.getNom());
        userFirstName.setText(unAdmin.getPrenom());
        userTitle.setText("Bienvenue " + unAdmin.getNom() + " " + unAdmin.getPrenom() + " !");
        userAddress.setText(unAdmin.getAdresse());
        userEmbauche.setText(unAdmin.getEmbauche());
    }

}
