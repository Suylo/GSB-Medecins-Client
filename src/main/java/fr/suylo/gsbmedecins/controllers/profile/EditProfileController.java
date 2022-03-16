package fr.suylo.gsbmedecins.controllers.profile;

import fr.suylo.gsbmedecins.controllers.MedecinController;
import fr.suylo.gsbmedecins.models.APIAccess;
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

        ObservableList<Departement> lesDepartements = APIAccess.getAllDepartements();
        ObservableList<Medecin> lesMedecins = APIAccess.getAllMedecins();
        Medecin unMedecin = APIAccess.getMedecinByID(getId());

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

            APIAccess.updateMedecin(
                    unMedecin.getId(),
                    doctorLastName,
                    doctorName,
                    doctorAddress,
                    doctorPhone,
                    doctorSpe);

            MedecinController medecinController = loader.getController();
            medecinController.reload();
        });
    }

}
