package fr.suylo.gsbmedecins.controllers.country;

import fr.suylo.gsbmedecins.models.APIAccess;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPaysController implements Initializable {

    @FXML
    public TextField countryName;
    @FXML
    public Label countryID;
    @FXML
    public Button buttonSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        APIAccess.getAllPays();

        countryID.setText("Ajout d'un pays");
    }
}
