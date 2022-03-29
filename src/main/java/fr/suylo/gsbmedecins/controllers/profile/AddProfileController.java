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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProfileController implements Initializable {

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
    private Integer valueDepartment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Medecin> lesMedecins = APIAccess.getAllMedecins();
        ObservableList<Departement> lesDepartements = APIAccess.getAllDepartements();

        ObservableSet<String> uniqueData = FXCollections.observableSet();
        uniqueData.add("");
        for (Medecin m : lesMedecins) {
            if (m.getSpe() != null) {
                uniqueData.add(m.getSpe());
            }
        }
        ObservableList<Departement> departements = FXCollections.observableArrayList();
        for (Departement de : lesDepartements) {
            departements.addAll(new Departement(de.getId(), de.getNom()));
        }
        doctorSpe.getItems().addAll(uniqueData);
        doctorDepartment.getItems().addAll(departements);
        doctorDepartment.setConverter(new StringConverter<Departement>() {
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

        doctorID.setText("Ajout d'un médecin");
        buttonSave.setOnAction(event -> {
            Pane doctorAdded = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctors.fxml"));
            try {
                doctorAdded = loader.load();
                doctorAdded.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
            stage.setTitle("GSB - Liste des médecins");
            stage.changeScene(doctorAdded);

            // Ajout de médecin
            APIAccess.addMedecin(doctorLastName, doctorName, doctorAddress, doctorPhone, doctorSpe, this.valueDepartment);
            MedecinController medecinController = loader.getController();
            medecinController.reload();
        });
    }
}
