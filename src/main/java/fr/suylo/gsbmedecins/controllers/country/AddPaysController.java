package fr.suylo.gsbmedecins.controllers.country;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Medecin;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPaysController implements Initializable {

    @FXML
    public TextField countryName;
    @FXML
    public Label countryID;
    @FXML
    public Button buttonSave;

    public void loadFields() {
        HttpResponse<JsonNode> apiResponsePays = null;
        try {
            apiResponsePays = Unirest.get("http://localhost:8080/api/v1/pays").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Pays[] lesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponsePays).getBody().toString()), Pays[].class);
        // Pour une future vérification et que le pays entré n'existe pas déjà
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFields();

        countryID.setText("Ajout d'un pays");
    }
}
