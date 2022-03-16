package fr.suylo.gsbmedecins.controllers.profile;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Objects;

public class ProfileController {

    @FXML
    public Label doctorLastName, doctorName, doctorAddress, doctorPhone, doctorSpe, doctorID, doctorDepartment;

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

    public void loadProfile() {
        // Récupération d'un seul médecin
        HttpResponse<JsonNode> apiResponse = null;
        try {
            apiResponse = Unirest.get("http://localhost:8080/api/v1/medecins/" + getId().toString()).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // Récupération d'un médecin au format Json
        Medecin unMedecin = new Gson().fromJson(String.valueOf(Objects.requireNonNull(apiResponse).getBody().toString()), Medecin.class);

        // Ajout des champs du médecin dans les champs FXML
        doctorID.setText("Profil N°" + unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());
        doctorDepartment.setText(unMedecin.getDepartement().getNom() + " (" + unMedecin.getDepartement().getPays().getNom() + ")");

        // Condition pour vérifier si une spécialité est null
        if (unMedecin.getSpe() == null) {
            doctorSpe.setText("// Spécialité non renseignée.");
        } else {
            doctorSpe.setText(unMedecin.getSpe());
        }
    }
}
