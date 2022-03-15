package fr.suylo.gsbmedecins.controllers.department;

import fr.suylo.gsbmedecins.models.APIAccess;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    public ComboBox<String> countrySelect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countrySelect.getItems().addAll(APIAccess.getAllPaysNom());

        departmentID.setText("Ajout d'un département");
    }
}
