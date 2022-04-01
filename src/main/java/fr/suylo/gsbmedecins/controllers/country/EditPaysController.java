package fr.suylo.gsbmedecins.controllers.country;

import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Pays;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lk.vivoxalabs.customstage.CustomStage;

import java.io.IOException;

public class EditPaysController {

    @FXML
    public TextField countryName;
    @FXML
    public Label countryNameError;

    @FXML
    public Button buttonSave;
    @FXML
    public Label countryID;
    private String countryOldValue;

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

    public void loadEditPays() {
        countryNameError.styleProperty().set("-fx-text-fill: red");


        Pays unPays = APIAccess.getPaysById(this.getId());

        countryID.setText("Modification du pays N°" + unPays.getId().toString());
        countryName.setText(unPays.getNom());
        countryOldValue = String.valueOf(countryName.getText());

        buttonSave.setOnAction(event -> {
            if (countryName.getText().isEmpty() || countryName.getText().trim().isEmpty()) {
                countryNameError.setText("Le nom du pays est obligatoire et ne peut être vide !");
            } else {
                ObservableList<Integer> paysValue = APIAccess.getPaysByNom(countryName.getText().trim());
                if (paysValue.size() == 0 || paysValue.size() == 1 && countryName.getText().equals(countryOldValue)) {
                    if (countryName.getText().matches("[a-zA-Z\\sàâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ-]{3,30}")) {
                        Pane paysEdited = null;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pays.fxml"));
                        try {
                            paysEdited = loader.load();
                            paysEdited.getStylesheets().add("fr/suylo/gsbmedecins/css/main.css");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CustomStage stage = ((CustomStage) buttonSave.getScene().getWindow());
                        stage.setTitle("GSB - Liste des pays");
                        stage.changeScene(paysEdited);

                        APIAccess.updatePays(this.getId(), countryName.getText());

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
