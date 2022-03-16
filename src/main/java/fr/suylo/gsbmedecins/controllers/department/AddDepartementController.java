package fr.suylo.gsbmedecins.controllers.department;

import fr.suylo.gsbmedecins.controllers.MedecinController;
import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Departement;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.collections.ObservableList;
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

public class AddDepartementController implements Initializable {

    @FXML
    public TextField departmentName;
    @FXML
    public Label departmentID;
    @FXML
    public Button buttonSave;
    @FXML
    public ComboBox<Pays> countrySelect;
    private Long valueCountry;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Pays> lesPaysNom = APIAccess.getAllPays();

        departmentID.setText("Ajout d'un département");
        countrySelect.getItems().addAll(lesPaysNom);
        countrySelect.setConverter(new StringConverter<Pays>() {
            @Override
            public String toString(Pays object) {
                return object.getNom();
            }

            @Override
            public Pays fromString(String string) {
                return countrySelect.getItems().stream().filter(ap -> ap.getNom().equals(string)).findFirst().orElse(null);
            }
        });
        countrySelect.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                this.valueCountry = newval.getId();
            }
        });

        buttonSave.setOnAction(event -> {
            Pane doctorAdded = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("departements.fxml"));
            try {
                doctorAdded = loader.load();
                doctorAdded.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
            stage.setTitle("GSB - Liste des départements");
            stage.changeScene(doctorAdded);

            APIAccess.addDepartement(departmentName, countrySelect);
            // Ajout de médecin
            DepartementController departementController = loader.getController();
            departementController.reload();
        });
    }
}
