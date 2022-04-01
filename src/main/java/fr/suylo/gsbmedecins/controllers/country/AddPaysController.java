package fr.suylo.gsbmedecins.controllers.country;

import fr.suylo.gsbmedecins.models.APIAccess;
import javafx.collections.ObservableList;
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
    public Label countryNameError;
    @FXML
    public Label countryID;
    @FXML
    public Button buttonSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        countryID.setText("Ajout d'un pays");
        countryNameError.styleProperty().set("-fx-text-fill: red");


        buttonSave.setOnAction(event -> {
            if (countryName.getText().isEmpty() || countryName.getText().trim().isEmpty()) {
                countryNameError.setText("Le nom du pays est obligatoire et ne peut être vide !");
            } else {
                ObservableList<Integer> paysValue = APIAccess.getPaysByNom(countryName.getText());
                if (paysValue.size() == 0) {
                    // countryName matches "[a-zA-Z\\sàâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ-]{3,30}"
                    if (countryName.getText().matches("[a-zA-Z\\sàâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ-]{3,30}")) {
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
                    } else {
                        countryNameError.setText("Le nom du pays ne doit contenir que des lettres (min. 3 ; max. 50)");
                    }
                } else {
                    countryNameError.setText("Le pays existe déjà !");
                }
            }
        });
    }
}
