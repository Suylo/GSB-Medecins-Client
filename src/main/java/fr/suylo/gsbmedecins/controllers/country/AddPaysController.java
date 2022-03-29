package fr.suylo.gsbmedecins.controllers.country;

import fr.suylo.gsbmedecins.models.APIAccess;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;
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

        countryID.setText("Ajout d'un pays");

        buttonSave.setOnAction(event -> {
            Pane addCountry = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pays.fxml"));
            try {
                addCountry = loader.load();
                addCountry.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
            stage.setTitle("GSB - Liste des pays");
            stage.changeScene(addCountry);

            APIAccess.addPays(countryName);

            PaysController paysController = loader.getController();
            paysController.reload();
        });
    }
}
