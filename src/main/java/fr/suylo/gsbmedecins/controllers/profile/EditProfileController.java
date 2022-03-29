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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;

public class EditProfileController {

    private final String speIsNull = "// Aucune spécialité complémentaire.";
    @FXML
    public TextField doctorLastName, doctorName, doctorAddress, doctorPhone;
    @FXML
    public Label doctorID;
    @FXML
    public ComboBox<String> doctorSpe;
    @FXML
    public ComboBox<Departement> doctorDepartment;
    @FXML
    public Button buttonSave;
    private Integer id;
    private Integer valueDepartment;

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
        uniqueData.add(this.speIsNull);
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null) {
                uniqueData.add(m.getSpe());
            }
        }

        ObservableList<Departement> departements = FXCollections.observableArrayList();
        for (Departement de : lesDepartements) {
            departements.addAll(new Departement(de.getId(), de.getNom()));
        }

        doctorID.setText("Modification du profil N°" + unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());
        doctorSpe.getItems().addAll(uniqueData);
        doctorDepartment.setValue(unMedecin.getDepartement());
        if (unMedecin.getSpe() == null) {
            doctorSpe.setValue(this.speIsNull);
        } else {
            doctorSpe.setValue(unMedecin.getSpe());
        }
        doctorDepartment.getItems().addAll(departements);
        doctorDepartment.setConverter(new StringConverter<>() {
            @Override
            public String toString(Departement object) {
                return object.getNom();
            }

            @Override
            public Departement fromString(String string) {
                return doctorDepartment.getItems().stream().filter(ap -> ap.getNom().equals(string)).findFirst().orElse(null);
            }
        });
        doctorDepartment.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                this.valueDepartment = newval.getId();
            }
        });

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
            if (doctorSpe.getValue().equals(this.speIsNull)) {
                doctorSpe.setValue(null);
            }

            System.out.println("TEST DEBUG VALUE ID :" + doctorDepartment.getValue().getNom());
            ObservableList<Integer> valueID = APIAccess.getDepartementByNom(doctorDepartment.getValue().getNom());
            System.out.println("TEST DEBUG UPDATE MEDECIN");
            APIAccess.updateMedecin(unMedecin.getId(), doctorLastName, doctorName, doctorAddress, doctorPhone, doctorSpe, valueID.get(0));

            MedecinController medecinController = loader.getController();
            medecinController.reload();
        });
    }

}
