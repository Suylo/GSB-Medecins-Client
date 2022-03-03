package fr.suylo.gsbmedecins.controllers.department;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddDepartementController implements Initializable {

    @FXML
    public TextField departmentName;
    @FXML
    public Label departmentID;
    @FXML
    public Button buttonSave;
    @FXML
    public ComboBox<String> countrySelect;

    public void loadFields() {
        HttpResponse<JsonNode> apiResponsePays = null;
        try {
            apiResponsePays = Unirest.get("http://localhost:8080/api/v1/pays").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Pays[] lesPays = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponsePays).getBody().toString()), Pays[].class);

        ObservableList<String> data = FXCollections.observableArrayList();
        for (Pays p : lesPays) {
            data.add(p.getNom());
        }

        countrySelect.getItems().addAll(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFields();

        departmentID.setText("Ajout d'un département");
    }
}
