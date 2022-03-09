package fr.suylo.gsbmedecins.controllers.profile;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.util.Objects;

public class EditProfileController {

    @FXML
    public TextField doctorLastName, doctorName, doctorAddress, doctorPhone;
    @FXML
    public Label doctorID;
    @FXML
    public ComboBox<String> doctorSpe;
    @FXML
    public ComboBox<Integer> doctorDepartment;
    @FXML
    public Button buttonSave;

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
        HttpResponse<JsonNode> apiResponseMedecins = null, apiResponseSpecialite = null, apiReponseDepartement = null;
        try {
            apiResponseMedecins = Unirest.get("http://localhost:8080/api/v1/medecins/" + getId().toString()).asJson();
            apiResponseSpecialite = Unirest.get("http://localhost:8080/api/v1/medecins").asJson();
            apiReponseDepartement = Unirest.get("http://localhost:8080/api/v1/departements").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Medecin unMedecin = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseMedecins).getBody().toString()), Medecin.class);
        Medecin[] lesMedecins = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponseSpecialite).getBody().toString()), Medecin[].class);
        Departement[] lesDepartements = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiReponseDepartement).getBody().toString()), Departement[].class);

        ObservableSet<String> uniqueData = FXCollections.observableSet();
        uniqueData.add("");
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null){
                uniqueData.add(m.getSpe());
            }
        }

        ObservableList<Integer> departements = FXCollections.observableArrayList();
        for (Departement de : lesDepartements) {
            departements.addAll(de.getId());
        }

        doctorID.setText("Modification du profil N°" + unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());
        doctorSpe.getItems().addAll(uniqueData);
        doctorSpe.setValue(unMedecin.getSpe());
        doctorDepartment.getItems().addAll(departements);

        buttonSave.setOnAction(event -> {
             editProfileWithAPI(unMedecin.getId());
             Pane doctorEditedNewList = null;
             FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctors.fxml"));
             try {
                 doctorEditedNewList = loader.load();
                 doctorEditedNewList.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
             } catch (IOException e) {
                 e.printStackTrace();
             }
             CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
             stage.setTitle("GSB - Liste des médecins");
             stage.changeScene(doctorEditedNewList);
         });
    }

    private void editProfileWithAPI(Integer id){
        Medecin newMedecin = new Medecin(
                doctorLastName.getText(),
                doctorName.getText(),
                doctorAddress.getText(),
                doctorPhone.getText(),
                doctorSpe.getValue(),
                new Departement(doctorDepartment.getValue())
        );
        try {
            Unirest.put("http://localhost:8080/api/v1/medecins/medecins/" + id)
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(newMedecin)).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
