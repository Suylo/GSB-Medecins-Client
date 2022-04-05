package fr.suylo.gsbmedecins.controllers.department;

import fr.suylo.gsbmedecins.models.APIAccess;
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
    public Label departmentNameError, countrySelectError;
    @FXML
    public Label departmentID;
    @FXML
    public Button buttonSave;
    @FXML
    public ComboBox<Pays> countrySelect;
    private Long valueCountry;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        departmentNameError.styleProperty().set("-fx-text-fill: red");
        countrySelectError.styleProperty().set("-fx-text-fill: red");

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
            ObservableList<Integer> departmentIfExist = APIAccess.getDepartementByNom(departmentName.getText().replace(" ", "+"));

            if (departmentName.getText().isEmpty() || departmentName.getText().trim().isEmpty() && countrySelect.getValue() == null) {
                departmentNameError.setVisible(true);
                countrySelectError.setVisible(true);
                departmentNameError.setText("Le nom du département est obligatoire");
                countrySelectError.setText("Le pays est obligatoire");
            } else if (!departmentName.getText().isEmpty() && countrySelect.getValue() == null) {
                countrySelectError.setText("Le pays est obligatoire");
            } else {
                if (departmentIfExist.size() == 0) {
                    // check if departmentName matches all letters, no numbers or special characters, min 3 characters and max 30 characters
                    if (departmentName.getText().matches("[a-zA-Z\\sàâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ-]{3,30}")) {
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

                        APIAccess.addDepartement(departmentName.getText(), countrySelect);

                        DepartementController departementController = loader.getController();
                        departementController.reload();
                    } else {
                        departmentNameError.setText("Le nom du département ne doit contenir que des lettres, (min. 3 ; max. 50)");
                    }
                } else {
                    departmentNameError.setText("Le nom du département existe déjà");
                }
            }

        });
    }
}
