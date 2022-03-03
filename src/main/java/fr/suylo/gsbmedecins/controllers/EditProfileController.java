package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Objects;

public class EditProfileController {

    @FXML
    public TextField doctorLastName, doctorName, doctorAddress, doctorPhone;
    @FXML
    public Label doctorID;
    @FXML
    public ComboBox<String> doctorSpe;

    private Integer id;

    public void loadData(Integer index) {
        this.setId(index);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void loadEditProfile() {
        HttpResponse<JsonNode> apiResponseMedecins = null, apiResponseSpecialite = null;
        try {
            apiResponseMedecins = Unirest.get("http://localhost:8080/api/v1/medecins/" + getId().toString()).asJson();
            apiResponseSpecialite = Unirest.get("http://localhost:8080/api/v1/medecins").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Medecin unMedecin = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseMedecins).getBody().toString()), Medecin.class);
        Medecin[] lesMedecins = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseSpecialite).getBody().toString()), Medecin[].class);

        ObservableSet<String> uniqueData = FXCollections.observableSet();
        uniqueData.add("");
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null){
                uniqueData.add(m.getSpe());
            }
        }

        doctorID.setText("Modification du profil N°" + unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());
        doctorSpe.getItems().addAll(uniqueData);
    }

}
