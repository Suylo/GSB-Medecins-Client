package fr.suylo.gsbmedecins.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    public Label doctorLastName;
    @FXML
    public Label doctorName;
    @FXML
    public Label doctorAddress;
    @FXML
    public Label doctorPhone;
    @FXML
    public Label doctorSpe;
    @FXML
    public Label doctorID;
    private Integer id;

    public void loadData(Integer index){
        this.setId(index);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INIT : " + getId());
    }


    public void setId(Integer id){
        this.id = id;
    }


    public Integer getId(){
        return this.id;
    }


    public void loadProfile(){

        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins/" + getId().toString()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération d'un médecin au format Json
        Medecin unMedecin = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin.class);
        System.out.println(unMedecin.toString());

        // Ajout des champs du médecin dans les champs FXML
        doctorID.setText(unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());

        // Condition pour vérifier si une spécialité est null
        if (unMedecin.getSpe() == null){
            doctorSpe.setText("//");
        } else {
            doctorSpe.setText(unMedecin.getSpe());
        }
    }


}
