package fr.suylo.gsbmedecins.controllers.profile;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddProfileController implements Initializable {

    @FXML
    public TextField doctorLastName, doctorName, doctorAddress, doctorPhone;
    @FXML
    public Label doctorID;
    @FXML
    public ComboBox<String> doctorSpe;
    @FXML
    public Button buttonSave;

    public void loadFields() {
        HttpResponse<JsonNode> apiResponseSpecialite = null;
        try {
            apiResponseSpecialite = Unirest.get("http://localhost:8080/api/v1/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Medecin[] lesMedecins = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseSpecialite).getBody().toString()), Medecin[].class);

        ObservableSet<String> uniqueData = FXCollections.observableSet();
        uniqueData.add("");
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null){
                uniqueData.add(m.getSpe());
            }
        }

        doctorSpe.getItems().addAll(uniqueData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFields();

        doctorID.setText("Ajout d'un médecin");
        buttonSave.setOnAction(event -> {
            addMedecin();
            Pane doctorAdded = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctors.fxml"));
            try {
                doctorAdded = loader.load();
                doctorAdded.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
            stage.setTitle("GSB - Ajout d'un médecin");
            stage.changeScene(doctorAdded);
        });
    }

    private void addMedecin(){
        Medecin newMedecin = new Medecin(
                doctorLastName.getText(),
                doctorName.getText(),
                doctorAddress.getText(),
                doctorPhone.getText(),
                doctorSpe.getValue()
        );
        try {
            Unirest.post("http://localhost:8080/api/v1/medecins/medecins")
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(newMedecin)).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
